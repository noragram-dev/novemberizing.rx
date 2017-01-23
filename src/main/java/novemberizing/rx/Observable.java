package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Executor;
import novemberizing.ds.func.Single;
import novemberizing.rx.functions.OnComplete;
import novemberizing.rx.functions.OnError;
import novemberizing.rx.functions.OnNext;
import novemberizing.rx.operators.Completion;
import novemberizing.rx.operators.Composer;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Sync;
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

        @Override
        protected void complete() {
            if(!__completed){
                __observable.emits.decrease();
            }
            super.complete();
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

        @Override
        protected void complete() {
            if(!__completed){
                __observable.emits.decrease(in.size());
            }
            super.complete();
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

    public static class Requests<T> extends Counter {
        private boolean __once = false;

        public void once(boolean v){ __once = v; }

        public boolean once(){ return __once; }
    }

    private final LinkedHashSet<Observer<T>> __observers = new LinkedHashSet<>();
    @Expose public final Counter emits = new Counter();
    @Expose public final Requests<T> requests = new Requests<>();
    @Expose protected T __current;
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
                Log.e(Tag, "= clear =");
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
        unsubscribe();
        return e;
    }

    protected T done(){
        if(__replayer!=null){
            __replayer.complete(__current);
        }
        unsubscribe();
        return __current;
    }

    private Req<T> __req(Req<T> req){
        requests.increase();
        req.set(this);
        Scheduler current = Scheduler.Self();
        if (current == __observableOn) {
            req.exec();
        } else {
            __observableOn.dispatch(req);
        }
        return req;
    }

    protected Req<T> req(Req<T> req){
        if(requests.__once){
           if(requests.get()==0) {
               req = __req(req);
           } else {
               req.set(this);
               requests.increase();
               req.error(new RuntimeException("already requested"));
               req.complete();
           }
        } else {
            req = __req(req);
        }
        return req;
    }

    protected Task<T, T> emit(T o){

        synchronized (this) {
            __current = snapshot(o);
        }
        emits.increase();
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
        emits.increase(objects.size());
        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);

        return task;
    }

    private Task<Collection<T>, T> foreach(T[] items){
        LinkedList<T> objects = new LinkedList<>();
        for(T item : items){
            objects.addLast(item);
        }
        emits.increase(objects.size());
        Emits<T> task = new Emits<>(objects, this);
        __observableOn.dispatch(task);

        return task;
    }

    private Task<Collection<T>, T> bulk(Collection<T> items){

        emits.increase(items.size());
        Emits<T> task = new Emits<>(items, this);
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

    protected void onStart(){

    }

    protected void onStop(){

    }

    public Observable<T> subscribe(OnNext<T> onNext){
        return subscribe(new Subscribers.Just<T>(){
            @Override
            public void onNext(T o){
                if(onNext!=null){
                    onNext.on(o);
                }
            }
        });
    }

    public Observable<T> subscribe(OnNext<T> onNext, OnError onError){
        return subscribe(new Subscribers.Just<T>(){
            @Override
            public void onNext(T o){
                if(onNext!=null){
                    onNext.on(o);
                }
            }
            @Override
            public void onError(Throwable e){
                if(onError!=null){
                    onError.on(e);
                }
            }
        });
    }

    public Observable<T> subscribe(OnNext<T> onNext, OnComplete onComplete){
        return subscribe(new Subscribers.Just<T>(){
            @Override
            public void onNext(T o){
                if(onNext!=null){
                    onNext.on(o);
                }
            }
            @Override
            public void onComplete(){
                if(onComplete!=null){
                    onComplete.on();
                }
            }
        });
    }

    public Observable<T> subscribe(OnNext<T> onNext, OnError onError, OnComplete onComplete){
        return subscribe(new Subscribers.Just<T>(){
            @Override
            public void onNext(T o){
                if(onNext!=null){
                    onNext.on(o);
                }
            }
            @Override
            public void onError(Throwable e){
                if(onError!=null){
                    onError.on(e);
                }
            }

            @Override
            public void onComplete(){
                if(onComplete!=null){
                    onComplete.on();
                }
            }
        });
    }

    public Observable<T> subscribe(Observer<T> observer){
        if(observer!=null){
            synchronized (__observers){
                if(__observers.add(observer)){
                    onSubscribe(observer,this);
                    if(__replayer!=null){
                        __replayer.replay(observer);
                    }
                    if(__observers.size()==1){
                        onStart();
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

    public <Z> Operator<T, Z> subscribe(Single<T, Z> f){ return subscribe(Operator.Op(f)); }

    public <Z> Operator<T, Z> subscribe(novemberizing.ds.on.Pair<Operator.Task<T, Z>,T> f){ return subscribe(Operator.Op(f)); }

    public <Z> Operator<T, Z> subscribe(Operator<T, Z> operator){
        if(operator!=null){
            synchronized (__observers){
                if(__observers.add(operator)){
                    onSubscribe(operator,this);
                    if(__replayer!=null){
                        __replayer.replay(operator);
                    }
                    if(__observers.size()==0){
                        onStop();
                    }
                } else {
                    Log.d(Tag, this, operator, "__observers.add(observer)==false");
                }
            }
        } else {
            Log.c(Tag, this, "observer==null");
        }
        return operator;
    }

//    public Observable<T> subscribe()


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

    public <S> Operator<S, T> previous(Operator<S, T> op){
        return (Operator<S, T>) op.subscribe(new Subscriber<T>() {
            @Override
            public void onNext(T o) {
                emit(o);
            }

            @Override
            public void onError(Throwable e) {
                error(e);
            }

            @Override
            public void onComplete() {
                complete();
            }
        });
    }

    public <S> Operator<S, T> previous(Single<S, T> f){ return previous(Operator.Op(f)); }

    public <S> Operator<S, T> previous(novemberizing.ds.on.Pair<Operator.Task<S, T>, S> f){
        return previous(Operator.Op(f));
    }

    public <Z> Operator<T, Z> next(Single<T, Z> f){ return subscribe(f); }
    public <Z> Operator<T, Z> next(Operator<T, Z> op){ return subscribe(op); }
    public <Z> Operator<T, Z> next(novemberizing.ds.on.Pair<Operator.Task<T, Z>,T> f){ return subscribe(f); }

    public Observable<T> once(novemberizing.ds.on.Single<T> f){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onNext(T o) {
                f.on(o);
                unsubscribe(this);
            }
        });
    }

    public Observable<T> on(novemberizing.ds.on.Single<T> f){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onNext(T o) { f.on(o); }
        });
    }

    public Observable<T> exception(novemberizing.ds.on.Single<Throwable> f){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onError(Throwable e) { f.on(e); }
        });
    }

    public Observable<T> completion(novemberizing.ds.on.Empty f){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onComplete() { f.on(); }
        });
    }

    public Observable<T> on(novemberizing.ds.on.Single<T> f, boolean once){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onNext(T o) {
                f.on(o);
                if(once){ unsubscribe(this); }
            }
        });
    }

    public Observable<T> exception(novemberizing.ds.on.Single<Throwable> f, boolean once){
        return subscribe(new Subscribers.Just<T>() {
            @Override
            public void onError(Throwable e) {
                f.on(e);
                if(once){ unsubscribe(this); }
            }
        });
    }

    public <Z> Sync<T, Z> sync(Single<T, Z> f){ return (Sync<T, Z>) subscribe(Operator.Sync(f)); }
    public <Z> Sync<T, Z> sync(novemberizing.ds.on.Pair<Operator.Task<T, Z>,T> f){ return (Sync<T, Z>) subscribe(Operator.Sync(f)); }

    public <U, Z> Composer<T, U, Z> compose(Observable<U> secondary, novemberizing.ds.func.Pair<T, U, Z> f){
        return (Composer<T, U, Z>) subscribe(Operator.Composer(secondary,f));
    }

    public <U, Z> Condition<T, U, Z> condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition , novemberizing.ds.func.Pair<T, U, Z> f){
        return (Condition<T, U, Z>) subscribe(Operator.Condition(observable,condition,f));
    }

    public <Z> Operator<T, Z> condition(Single<T, Boolean> condition, Single<T, Z> f){
        return subscribe(Operator.Condition(condition, f));
    }

    public <U, Z> Completion<T, U, Z> completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition , novemberizing.ds.func.Pair<T, U, Z> f){
        return (Completion<T, U, Z>) subscribe(Operator.Completion(observable,condition,f));
    }

    public <U, Z> Completion<T, U, Z> completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Z> f){
        return (Completion<T, U, Z>) subscribe(Operator.Completion(observable, null,f));
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

    public static <T> Task<Collection<T>,T> Bulk(Observable<T> observable, Collection<T> list){ return observable.bulk(list); }


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
