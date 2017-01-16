package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.ds.Task;
import novemberizing.rx.functions.OnComplete;
import novemberizing.rx.functions.OnError;
import novemberizing.rx.functions.OnNext;
import novemberizing.rx.observables.Just;
import novemberizing.rx.operators.Condition;
import novemberizing.util.Log;

import java.util.LinkedHashSet;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public class Observable<T> {

    private static final String Tag = "Observable";

    /**
     *
     * @author novemberizing, me@novemberizing.net
     * @since 2017. 1. 14
     */
    protected static class ObservableOn<T> extends Task {
        private Observable<T> __observable;
        private T __o;
        private Throwable __e;
        private boolean __completed;

        public ObservableOn(Observable<T> observable, T o, Throwable e, boolean completed){
            __observable = observable;
            __o = o;
            __e = e;
            __completed = completed;
        }

        @Override
        public void execute() {
            if(!__completed) {
                __observable.__next(__o);
            } else if(__e!=null){
                __observable.__error(__e);
            } else {
                __observable.__complete();
            }
            complete();
        }
    }

    /**
     *
     * @author novemberizing, me@novemberizing.net
     * @since 2017. 1. 14
     */
    protected static class ObserveOn<T> extends Task {
        private Observer<T> __observer;
        private T __o;
        private Throwable __e;
        private boolean __completed;

        public ObserveOn(Observer<T> observer, T o, Throwable e, boolean completed){
            __observer = observer;
            __o = o;
            __e = e;
            __completed = completed;
        }

        @Override
        public void execute() {
            if(!__completed) {
                __observer.onNext(__o);
            } else if(__e!=null){
                __observer.onError(__e);
            } else {
                __observer.onComplete();
            }
            complete();
        }
    }

    protected final LinkedHashSet<Observer<T>> __observers = new LinkedHashSet<>();
    protected Scheduler __observableOn = Scheduler.New();
    protected T __current = null;
    protected boolean __completed = false;
    protected Throwable __exception = null;

    protected T snapshot(T o){
        Log.f(Tag, this, o);
        return o;
    }

    protected Observable<T> emit(T o){
        Log.f(Tag, this, o);

        __observableOn.dispatch(new ObservableOn<>(this,  snapshot(o), null, false));

        return this;
    }

    protected void __next(T o){
        Log.f(Tag, this, o);

        __exception = null;
        __current = snapshot(o);
        __completed = false;

        for(Observer<T> observer : __observers) {
            Scheduler observeOn = observer.observeOn();
            if(observeOn==Scheduler.Self()){
                try {
                    observer.onNext(snapshot(o));
                } catch(Exception e){
                    observer.onError(e);
                }
            } else {
                observeOn.dispatch(new ObserveOn<>(observer, snapshot(o), null, false));
            }
        }
    }

    protected Observable<T> error(Throwable e){
        Log.f(Tag, this, e);

        __observableOn.dispatch(new ObservableOn<>(this, null, e, true));

        return this;
    }

    protected void __error(Throwable e){
        Log.f(Tag, this, e);

        __exception = e;
        __current = null;
        __completed = true;
        for(Observer<T> observer : __observers) {
            Scheduler observeOn = observer.observeOn();
            if(observeOn==Scheduler.Self()){
                observer.onError(e);
            } else {
                observeOn.dispatch(new ObserveOn<>(observer, null, e, true));
            }
        }
    }

    protected Observable<T> complete(){
        Log.f(Tag, this);

        __observableOn.dispatch(new ObservableOn<>(this, null, null, true));

        return this;
    }

    protected Observable<T> complete(T o){
        Log.f(Tag, this);

        __observableOn.dispatch(new ObservableOn<>(this, o, null, true));

        return this;
    }

    protected void __complete(){
        Log.f(Tag, this);

        __exception = null;
        __current = null;
        __completed = true;

        for(Observer<T> observer : __observers) {
            Scheduler observeOn = observer.observeOn();
            if(observeOn==Scheduler.Self()){
                observer.onComplete();
            } else {
                observeOn.dispatch(new ObserveOn<>(observer, null, null, true));
            }
        }
    }

    protected void onSubscribed(Operator<T, ?> operator){

    }

    protected void onSubscribed(Observer<T> operator){

    }

    public final <Z> Operator<T, Z> subscribe(Operator<T, Z> operator){
        Log.f(Tag, this, operator);
        if(operator!=null){
            synchronized (__observers){
                if(__observers.add(operator.subscriber)){
                    operator.subscriber.onSubscribe(this);
                    onSubscribed(operator.subscriber);
                } else {
                    Log.c(Tag, new RuntimeException("__observers.add(observer)==false"));
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
        return operator;
    }

    public final Observable<T> subscribe(Observer<T> observer){
        Log.f(Tag, this, observer);
        if(observer!=null){
            synchronized (__observers){
                if(__observers.add(observer)){
                    observer.onSubscribe(this);
                    onSubscribed(observer);
                } else {
                    Log.c(Tag, new RuntimeException("__observers.add(observer)==false"));
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
        return this;
    }


    public <Z> Operator<T, Z> append(Operator<T, Z> op){ return subscribe(op); }
    public <Z> Operator<T, Z> append(Func<T, Z> f){ return subscribe(Operator.Op(f)); }
    public Observable<T> append(Runnable r) {
        return subscribe(new Subscribers.Just<T>(){
            @Override
            public void onNext(T o){
                r.run();
            }
        });
    }


    public <Z> Condition<T, Z> condition(Func<T, Boolean> condition, Func<T, Z> f){
        return (Condition<T, Z>) subscribe(Operator.Condition(condition, f));
    }

    public <Z, U> Condition<T, Z> condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition, novemberizing.ds.func.Pair<T, U, Z> f){
        return (Condition<T, Z>) subscribe(Operator.Condition(observable, condition, f));
    }


    public final Observable<T> subscribe(OnNext<T> next, OnError error, OnComplete complete){
        return subscribe(new Subscriber<T>() {
            @Override public void onNext(T o) { next.on(o); }
            @Override public void onComplete() { complete.on(); }
            @Override public void onError(Throwable e) { error.on(e); }
        });
    }

    public final Observable<T> subscribe(OnNext<T> next){
        return subscribe(new Subscriber<T>() {
            @Override public void onNext(T o) { next.on(o); }
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    public final Observable<T> unsubscribe(Observer<T> observer){
        Log.f(Tag, this, observer);
        if(observer!=null){
            synchronized (__observers){
                if(__observers.remove(observer)){
                    observer.onUnsubscribe(this);
                } else {
                    Log.c(Tag, new RuntimeException("__observers.remove(observer)==false"));
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
        return this;
    }

    public static <T> Observable<T> emit(Observable<T> observable, T o){
        return observable!=null ? observable.emit(o) : null;
    }

    public static Observable<?> complete(Observable<?> observable){
        return observable!=null ? observable.complete() : null;
    }

    public static Observable<?> error(Observable<?> observable, Throwable e){
        return observable!=null ? observable.error(e) : null;
    }

    public static <T> Just<T> Just(){ return new Just<>(); }
}
