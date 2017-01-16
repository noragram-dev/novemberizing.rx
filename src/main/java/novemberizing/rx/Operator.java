package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.rx.operators.CompletionPort;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Sync;
import novemberizing.util.Log;

import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Operator<T, Z> extends Observable<Z> {

    private static final String Tag = "Operator";

    public static class Local<T, Z> extends Task<T, Z> {
        protected Operator<T, Z> __op;

        public Local(T in, Z out, Operator<T, Z> op) {
            super(in, out);
            __op = op;
        }

        @Override
        public void execute() {
            __op.on(this);
        }

        @Override
        protected void complete(){
            __op.internal.emit(this);
            super.complete();
        }

        @Override
        public void error(Throwable e){
            __op.internal.error(e);
            super.error(e);
        }

        public void done(Z o){
            out = o;
            complete();
        }
    }

    public static class Internal<T, Z> extends Observable<Local<T, Z>> {
        protected Operator<T, Z> parent;

        protected Local<T, Z> exec(T o){
            Local<T, Z> task = new Local<>(o, null, parent);

            __observableOn.dispatch(task);

            return task;
        }
//
//
//        public Tasks foreach(T o, T... items){
//            Tasks tasks = new Tasks(new LinkedList<>(), new LinkedList<>());
//
//            tasks.add(internal.exec(o));
//            for(T item : items){
//                tasks.add(internal.exec(item));
//            }
//            return tasks;
//        }
//
//        public Tasks foreach(T[] items){
//            Tasks tasks = new Tasks(new LinkedList<>(), new LinkedList<>());
//            for(T item : items){
//                tasks.add(internal.exec(item));
//            }
//            return tasks;
//        }

        protected Observable<Local<T, Z>> emit(Local<T, Z> o){
            Log.f(Tag, this, o);

            __next(o);

            return this;
        }

        protected void initialize(){
            subscribe(new Subscriber<Local<T, Z>>() {
                @Override
                public void onNext(Local<T, Z> task) {
                    parent.emit(task.out);
                }

                @Override
                public void onComplete() {
                    parent.complete();
                }

                @Override
                public void onError(Throwable e) {
                    parent.error(e);
                }
            });
        }

        public Internal(Operator<T, Z> p){
            this.parent = p;
            initialize();
        }
    }

    protected Internal<T, Z> internal;
    protected Subscriber<T> subscriber = new Subscriber<T>() {
        @Override
        public void onNext(T o) {
            exec(o);
        }

        @Override
        public void onComplete() {
            complete();
        }

        @Override
        public void onError(Throwable e) {
            error(e);
        }
    };

    public Operator(){
        initialize();
    }

    protected Internal<T, Z> initialize(){
        return internal = new Internal<>(this);
    }

    public Local<T, Z> exec(T o){
        return internal.exec(o);
    }

    public Tasks foreach(T o, T... items){
        Tasks tasks = new Tasks(new LinkedList<>(), new LinkedList<>());
        tasks.add(internal.exec(o));
        for(T item : items){
            tasks.add(internal.exec(item));
        }
        return tasks;
    }

    public Tasks foreach(T[] items){
        Tasks tasks = new Tasks(new LinkedList<>(), new LinkedList<>());
        for(T item : items){
            tasks.add(internal.exec(item));
        }
        return tasks;
    }

    protected abstract void on(Local<T, Z> task);

    @Override
    protected Observable<Z> emit(Z o){
        Log.f(Tag, this, o);
        if(__observableOn==Scheduler.Self()) {
            __next(o);
        } else {
            __observableOn.dispatch(new ObservableOn<>(this, snapshot(o), null, false));
        }

        return this;
    }



    public final Operator<T, Z> subscribe(Subscribers.Task<T, Z> subscriber){
        Log.f(Tag, this, subscriber);
        if(subscriber!=null){
            synchronized (internal.__observers){
                if(internal.__observers.add(subscriber)){
                    subscriber.onSubscribe(internal);
                } else {
                    Log.c(Tag, new RuntimeException("internal.__observers.add(subscriber)==false"));
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
        return this;
    }

    public final Operator<T, Z> unsubscribe(Subscribers.Task<T, Z> subscriber){
        Log.f(Tag, this, subscriber);
        if(subscriber!=null){
            synchronized (internal.__observers){
                if(internal.__observers.remove(subscriber)){
                    subscriber.onUnsubscribe(internal);
                } else {
                    Log.c(Tag, new RuntimeException("internal.__observers.remove(subscriber)==false"));
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
        return this;
    }


    public Operator<T, Z> append(Callable<Z> c) {
        return subscribe(new Subscribers.Task<T, Z>(){
            @Override
            public void onNext(Operator.Local<T, Z> task){
                try {
                    task.done(task.out = c.call());
                } catch (Exception e) {
                    onError(e);
                }
            }
        });
    }

    public static <T, Z> Operator<T, Z> Op(Func<T, Z> f){
        return new Operator<T, Z>() {
            @Override
            public void on(Local<T, Z> task) {
                task.done(task.out = f.call(task.in));
            }
        };
    }

    public static <T, Z> Sync<T, Z> Sync(Func<T, Z> f){
        return new Sync<T, Z>(){
            @Override
            protected void on(Local<T, Z> task) {
                task.done(f.call(task.in));
            }
        };
    }

    public static <T, Z> Condition<T, Z> Condition(Func<T, Boolean> condition, Func<T, Z> f){
        return new Condition<>(condition, f);
    }

    public static <T, U, Z> Condition<T, Z> Condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition, novemberizing.ds.func.Pair<T, U, Z> f){
        return new Condition<>(observable, condition, f);
    }

    public static CompletionPort CompletionPort(){ return new CompletionPort(); }

}
