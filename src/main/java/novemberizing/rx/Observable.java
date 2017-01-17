package novemberizing.rx;

import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Observable<T> {
    private static final String Tag = "Observable";

    protected static class Emit<T> extends Task<T, T> {
        protected Observable<T> __observable;

        public Emit(T in, Observable<T> observable) {
            super(in, new Observable<>());
            __observable = observable;
            __completionPort.replay(Infinite);
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            if(__observable.__replayer!=null) {
                __observable.__replayer.add(in);
            }
            synchronized (__observable.__observers) {
                for (Observer<T> observer : __observable.__observers) {
                    Scheduler observeOn = observer.observeOn();
                    if (current == observeOn) {
                        try {
                            observer.onNext(__observable.snapshot(in));
                        } catch(Exception e){
                            observer.onError(e);
                        }
                    } else {
                        observeOn.dispatch(new Subscriber.Observe<>(__observable.snapshot(in), observer));
                    }
                }
            }
            out = __observable.snapshot(in);
            complete();
        }
    }

    protected static class Emits<T> extends Task<Collection<T>, Collection<T>> {
        protected Observable<T> __observable;

        public Emits(Collection<T> in, Observable<T> observable) {
            super(in, new Observable<>());
            __observable = observable;
            __completionPort.replay(Infinite);
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            if(__observable.__replayer!=null) {
                __observable.__replayer.all(in);
            }
            synchronized (__observable.__observers) {
                for (Observer<T> observer : __observable.__observers) {
                    for(T item : in) {
                        Scheduler observeOn = observer.observeOn();
                        if (current == observeOn) {
                            try {
                                observer.onNext(__observable.snapshot(item));
                            } catch (Exception e) {
                                observer.onError(e);
                            }
                        } else {
                            observeOn.dispatch(new Subscriber.Observe<>(__observable.snapshot(__observable.snapshot(item)), observer));
                        }
                    }
                }
            }
            out = in;
            complete();
        }
    }

    protected static class Error<T> extends Task<T, Throwable> {
        protected Observable<T> __observable;
        protected Throwable __exception;

        public Error(Throwable e, Observable<T> observable) {
            super(null);
            __observable = observable;
            __exception = e;
        }

        public Error(Throwable e, Observable<Task<T, Throwable>> completionPort,Observable<T> observable) {
            super(null, completionPort);
            __observable = observable;
            __exception = e;
            __completionPort.replay(Infinite);
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            if(__observable.__replayer!=null) {
                __observable.__replayer.error(__exception);
            }
            synchronized (__observable.__observers) {
                for (Observer<T> observer : __observable.__observers) {
                    Scheduler observeOn = observer.observeOn();
                    if (current == observeOn) {
                        observer.onError(__exception);
                    } else {
                        observeOn.dispatch(new Subscriber.Error<>(__exception, observer));
                    }
                }
            }
            out = __exception;
            complete();
        }
    }

    protected static class Complete<T> extends Task<T, T> {
        protected Observable<T> __observable;

        public Complete(T current, Observable<T> observable) {
            super(current);
            __observable = observable;
        }

        public Complete(T current, Observable<Task<T, T>> completionPort, Observable<T> observable) {
            super(current, completionPort);
            __observable = observable;
            __completionPort.replay(Infinite);
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            if(__observable.__replayer!=null) {
                __observable.__replayer.complete(in);
            }
            synchronized (__observable.__observers) {
                for (Observer<T> observer : __observable.__observers) {
                    Scheduler observeOn = observer.observeOn();
                    if (current == observeOn) {
                        observer.onComplete();
                    } else {
                        observeOn.dispatch(new Subscriber.Complete<>(in, observer));
                    }
                }
            }
            out = in;
            complete();
        }
    }


    private final LinkedHashSet<Observer<T>> __observers = new LinkedHashSet<>();
    protected T __current;
    protected Replayer<T> __replayer;
    protected Scheduler __observableOn = Scheduler.New();

    protected T snapshot(T o){ return o; }

    protected Observable<Task<T, T>> emit(T o){
        synchronized (this) {
            __current = snapshot(o);
        }
        Emit<T> task = new Emit<>(o, this);
        __observableOn.dispatch(task);
        return task.__completionPort;
    }

    protected Observable<Task<Collection<T>, Collection<T>>> foreach(T o, T... items){
        LinkedList<T> objects = new LinkedList<>();
        objects.addLast(o);
        for(T item : items){
            objects.addLast(item);
        }

        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);
        return task.__completionPort;
    }

    protected Observable<Task<Collection<T>, Collection<T>>> foreach(T[] items){
        LinkedList<T> objects = new LinkedList<>();
        for(T item : items){
            objects.addLast(item);
        }

        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);
        return task.__completionPort;
    }

    protected Observable<Task<T, Throwable>> error(Throwable e){
        Error<T> task = new Error<>(e, this);
        __observableOn.dispatch(task);
        return task.__completionPort;
    }

    protected Observable<Task<T, T>> complete(){
        Complete<T> task = new Complete<>(snapshot(__current), this);
        __observableOn.dispatch(task);
        return task.__completionPort;
    }

    public Observable<T> subscribe(Observer<T> observer){
        if(observer!=null){
            synchronized (__observers){
                if(__observers.add(observer)){
                    observer.onSubscribe(this);
                    if(__replayer!=null){
                        __replayer.replay(observer);
                    }
                } else {
                    Log.d(Tag, this, observer, "__observers.add(observer)==false");
                }
            }
        } else {
            Log.c(Tag, this, "observer==null");
        }
        return this;
    }

    public Observable<T> unsubscribe(Observer<T> observer){
        if(observer!=null){
            synchronized (__observers){
                if(__observers.remove(observer)){
                    observer.onUnsubscribe(this);
                } else {
                    Log.d(Tag, this, observer, "__observers.remove(observer)==false");
                }
            }
        } else {
            Log.c(Tag, this, "observer==null");
        }
        return this;
    }

    public Observable<T> unsubscribe(){
        synchronized (__observers) {
            for (Observer<T> observer : __observers) {
                unsubscribe(observer);
            }
        }
        return this;
    }

    protected Observable<T> replay(int limit){
        if(limit==0){
            __replayer = null;
        } else if(__replayer==null){
            __replayer = new Replayer<>(limit);
        } else {
            __replayer.limit(limit);
        }
        return this;
    }

    public static <T> Observable<Task<T, T>> emit(Observable<T> observable, T o){ return observable.emit(o); }

    public static <T> Observable<Task<Collection<T>, Collection<T>>> foreach(Observable<T> observable, T o, T... items){ return observable.foreach(o, items); }

    public static <T> Observable<Task<Collection<T>, Collection<T>>> foreach(Observable<T> observable, T[] items){ return observable.foreach(items); }


    public static <T> Observable<Task<T, T>> complete(Observable<T> observable) {
        Complete<T> task = new Complete<>(observable.snapshot(observable.__current), new Observable<>() ,observable);
        observable.__observableOn.dispatch(task);
        return task.__completionPort;
    }

    public static <T> Observable<Task<T, Throwable>> error(Observable<T> observable, Throwable e){
        Error<T> task = new Error<>(e, new Observable<>() ,observable);
        observable.__observableOn.dispatch(task);
        return task.__completionPort;
    }

}
