package novemberizing.rx;

import novemberizing.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Observable<T> {
    private static final String Tag = "Observable";

    protected static class Emit<T> extends Task<T, T> {
        protected Observable<T> __observable;

        public Emit(T in, Observable<T> observable) {
            super(in);
            __observable = observable;
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            synchronized (__observable.__observers) {
                next(__observable.set(in));
                for (Observer<T> observer : __observable.__observers) {
                    Scheduler observeOn = observer.observeOn();
                    if (current == observeOn) {
                        try {
                            observer.onNext(__observable.get());
                        } catch(Exception e){
                            observer.onError(e);
                        }
                    } else {
                        observeOn.dispatch(new Subscriber.Observe<>(__observable.get(), observer));
                    }
                }
            }
            complete();
        }
    }

    protected static class Emits<T> extends Task<Collection<T>, T> {
        protected Observable<T> __observable;

        public Emits(Collection<T> in, Observable<T> observable) {
            super(in);
            __observable = observable;
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            synchronized (__observable.__observers) {
                for(T item : in) {
                    next(__observable.set(item));
                    for (Observer<T> observer : __observable.__observers) {
                        Scheduler observeOn = observer.observeOn();
                        if (current == observeOn) {
                            try {
                                observer.onNext(__observable.get());
                            } catch (Exception e) {
                                observer.onError(e);
                            }
                        } else {
                            observeOn.dispatch(new Subscriber.Observe<>(__observable.get(), observer));
                        }
                    }
                }
            }
            complete();
        }
    }

    protected static class Error<T> extends Task<T, T> {
        protected Observable<T> __observable;
        protected Throwable __exception;

        public Error(Throwable e, Observable<T> observable) {
            super(null);
            __observable = observable;
            __exception = e;
        }


        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
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
            error(__observable.exception(__exception));
            complete();
        }
    }

    protected static class Complete<T> extends Task<T, T> {
        protected Observable<T> __observable;

        public Complete(Observable<T> observable) {
            super(null);
            __observable = observable;
        }

        @Override
        public void execute() {
            Scheduler current = Scheduler.Self();
            synchronized (__observable.__observers) {
                for (Observer<T> observer : __observable.__observers) {
                    Scheduler observeOn = observer.observeOn();
                    if (current == observeOn) {
                        observer.onComplete();
                    } else {
                        observeOn.dispatch(new Subscriber.Complete<>(__observable.get(),observer));
                    }
                }
            }
            __observable.done();
            complete();
        }
    }

    private final LinkedHashSet<Observer<T>> __observers = new LinkedHashSet<>();
    protected T __current;
    protected Replayer<T> __replayer;
    protected Scheduler __observableOn = Scheduler.New();
    protected boolean __completed = false;

    protected Observable(Replayer<T> replayer){
        __replayer = replayer;
        __current = __replayer.last();
    }

    public Observable(){
        __replayer = null;
        __current = null;
    }

    protected Scheduler observableOn(){ return __observableOn; }

    public Observable<T> observableOn(Scheduler scheduler){
        __observableOn = scheduler;
        return this;
    }

    protected T snapshot(T o){ return o; }

    protected T get(){ return snapshot(__current); }
    protected T set(T v){
        if(__completed) {
            if(__replayer!=null) {
                __replayer.clear();
            }
            __completed = false;
        }
        __current = snapshot(v);
        if(__replayer!=null) {
            __replayer.add(snapshot(__current));
        }
        return snapshot(__current);
    }

    protected Throwable exception(Throwable e){
        if(__replayer!=null){
            __replayer.error(e);
        }
        __completed = true;
        unsubscribe();
        return e;
    }

    protected T done(){
        if(__replayer!=null){
            __replayer.complete(__current);
        }
        __completed = true;
        unsubscribe();
        return __current;
    }

    protected Task<T, T> emit(T o){

        synchronized (this) {
            __current = snapshot(o);
        }
        Emit<T> task = new Emit<>(o, this);
        __observableOn.dispatch(task);
        return task;
    }

    @SafeVarargs
    private final Task<Collection<T>, T> foreach(T o, T... items){

        LinkedList<T> objects = new LinkedList<>();
        objects.addLast(o);
        for(T item : items){
            objects.addLast(item);
        }

        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);
        return task;
    }

    private final Task<Collection<T>, T> foreach(T[] items){
        LinkedList<T> objects = new LinkedList<>();
        for(T item : items){
            objects.addLast(item);
        }

        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);
        return task;
    }

    protected Task<T, T> error(Throwable e){
        Error<T> task = new Error<>(e, this);
        __observableOn.dispatch(task);
        return task;
    }

    protected Task<T, T> complete(){
        Complete<T> task = new Complete<>(this);
        __observableOn.dispatch(task);
        return task;
    }

    public Observable<T> subscribe(Observer<T> observer){
        if(observer!=null){
            synchronized (__observers){
                if(__observers.add(observer)){
                    onSubscribe(observer,this);
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
                    onUnsubscribe(observer,this);
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
            Iterator<Observer<T>> it = __observers.iterator();
            while(__observers.size()>0 && it.hasNext()){
                Observer<T> observer = it.next();
                unsubscribe(observer);
                it = __observers.iterator();
            }
        }
        return this;
    }

    public Observable<T> replay(int limit){
        if(limit==0){
            __replayer = null;
        } else if(__replayer==null){
            __replayer = new Replayer<>(limit);
        } else {
            __replayer.limit(limit);
        }
        return this;
    }

    public static <T> Task<T, T> Emit(Observable<T> observable, T o){ return observable.emit(o); }

    @SafeVarargs
    public static <T> Task<Collection<T>, T> Foreach(Observable<T> observable, T o, T... items){ return observable.foreach(o, items); }

    public static <T> Task<Collection<T>, T> Foreach(Observable<T> observable, T[] items){ return observable.foreach(items); }


    public static <T> Task<T, T> Complete(Observable<T> observable) { return observable.complete(); }

    public static <T> Task<T, T> Error(Observable<T> observable, Throwable e){ return observable.error(e); }

    protected static <T> void onSubscribe(Observer<T> observer, Observable<T> observable){
        if(observer instanceof Operator){
            Operator<T, ?> operator = (Operator<T, ?>) observer;
            operator.onSubscribe(observable);
        } else if(observer!=null){
            Subscriber<T> subscriber = (Subscriber<T>) observer;
            subscriber.onSubscribe(observable);
        }
    }

    protected static <T> void onUnsubscribe(Observer<T> observer, Observable<T> observable){
        if(observer instanceof Operator){
            Operator<T, ?> operator = (Operator<T, ?>) observer;
            operator.onUnsubscribe(observable);
        } else if(observer!=null){
            Subscriber<T> subscriber = (Subscriber<T>) observer;
            subscriber.onUnsubscribe(observable);
        }
    }

}
