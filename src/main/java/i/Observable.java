package i;

import novemberizing.util.Log;

import java.util.LinkedHashSet;

import static i.Constant.Infinite;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public abstract class Observable<T> {
    private static final String Tag = "Observable";

    protected static class Next<T> extends Command {
        private Observer<T> __observer;
        private T __o;

        public Next(Observer<T> observer, T o){
            __observer = observer;
            __o = o;
        }

        @Override
        public void execute() {
            try {
                __observer.onNext(__o);
            } catch(Exception first){
                try {
                    __observer.onError(first);
                } catch(Exception second){
                    Log.e(Tag, second);
                }
            }
            complete();
        }
    }

    protected static class Error<T> extends Command {
        private Observer<T> __observer;
        private Throwable __e;

        public Error(Observer<T> observer, Throwable e){
            __observer = observer;
            __e = e;
        }

        @Override
        public void execute() {
            try {
                __observer.onError(__e);
            } catch(Exception e){
                Log.e(Tag, e);
            }
            complete();
        }
    }

    protected static class Complete<T> extends Command {
        private Observer<T> __observer;

        public Complete(Observer<T> observer){
            __observer = observer;
        }

        @Override
        public void execute() {
            try {
                __observer.onComplete();
            } catch(Exception first){
                try {
                    __observer.onError(first);
                } catch(Exception second){
                    Log.e(Tag, second);
                }
            }
            complete();
        }
    }

    protected final LinkedHashSet<Observer<T>> __observers = new LinkedHashSet<>();
    protected Scheduler __publishOn;
    protected Scheduler __subscribeOn;
    protected T __current;
    protected Replayer<T> __replayer;

    protected T snapshot(T o){ return o; }

    public void update(T o){
        synchronized (this) {
            __current = snapshot(o);
            if(__replayer!=null){
                __replayer.add(snapshot(o));
            }
            next(o);
        }
    }

    public Observable<T> replay(int limit){
        if(limit==Infinite || limit>0){
            if(__replayer==null){
                __replayer = new Replayer<>(limit);
            } else {
                __replayer.limit(limit);
            }
        } else {
            __replayer = null;
        }
        return this;
    }

    protected void next(T o){
        synchronized (__observers){
            for(Observer<T> observer : __observers){
                Scheduler scheduler = observer.observeOn()==null ? (__publishOn==null ? Scheduler.Local() : __publishOn) : observer.observeOn();
                scheduler.dispatch(new Next<>(observer, snapshot(o)));
            }
        }
    }
    public void error(Throwable e){
        synchronized (this) {
            if (__replayer != null) {
                __replayer.error(e);
            }
        }
        synchronized (__observers){
            for(Observer<T> observer : __observers){
                Scheduler scheduler = observer.observeOn()==null ? (__publishOn==null ? Scheduler.Local() : __publishOn) : observer.observeOn();
                scheduler.dispatch(new Error<>(observer, e));
            }
        }
    }
    public void complete(){
        synchronized (this) {
            if (__replayer != null) {
                __replayer.complete();
            }
        }
        synchronized (__observers){
            for(Observer<T> observer : __observers){
                Scheduler scheduler = observer.observeOn()==null ? (__publishOn==null ? Scheduler.Local() : __publishOn) : observer.observeOn();
                scheduler.dispatch(new Complete<>(observer));
            }
        }
    }

    synchronized private boolean __subscribe(Observer<T> observer){
        boolean ret;
        synchronized (__observers){
            ret = __observers.add(observer);
        }
        return ret;
    }

    synchronized private boolean __unsubscribe(Observer<T> observer){
        boolean ret;
        synchronized (__observers){
            ret = __observers.remove(observer);
        }
        return ret;
    }

    public Observable<T> subscribe(Observer<T> observer){
        if(observer!=null){
            if(__subscribe(observer)){
                observer.onSubscribe(this);
                synchronized (this) {
                    if (__replayer != null) {
                        __replayer.on(observer);
                    }
                }
            }
        } else {
            Log.e(Tag, this, new RuntimeException("observer==null"));
        }
        return this;
    }

    public Observable<T> unsubscribe(Observer<T> observer){
        if(observer!=null){
            if(__unsubscribe(observer)){
                observer.onUnsubscribe(this);
            }
        } else {
            Log.e(Tag, this, new RuntimeException("observer==null"));
        }
        return this;
    }

    public Scheduler publishOn(){ return __publishOn; }

    synchronized public Observable<T> publishOn(Scheduler scheduler){
        __publishOn = scheduler;
        return this;
    }
}
