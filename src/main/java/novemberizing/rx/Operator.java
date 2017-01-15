package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Operator<T, U> extends Observable<U> {

    private static final String Tag = "Operator";

    public static class Local<T, U> extends Task<T, U> {
        protected Operator<T, U> __op;

        public Local(T in) {
            super(in);
        }

        public Local(T in, Operator<T, U> op) {
            super(in);
            __op = op;
        }

        protected Local(T in, U out) {
            super(in, out);
        }

        protected Local(T in, U out, Operator<T, U> op) {
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

        public void done(U o){
            out = o;
            complete();
        }
    }

    public static class Internal<T, U> extends Observable<Local<T, U>> {
        protected Operator<T, U> parent;
        protected Local<T, U> exec(T o){
            Local<T, U> task = new Local<>(o, parent);

            __observableOn.dispatch(task);

            return task;
        }

        protected Observable<Local<T, U>> emit(Local<T, U> o){
            Log.f(Tag, this, o);

            __next(o);

            return this;
        }

        protected Internal(Operator<T, U> parent){
            this.parent = parent;
        }
    }

    protected Internal<T, U> internal;
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
        internal = new Internal<>(this);
        internal.subscribe(new Subscriber<Local<T, U>>() {
            @Override
            public void onNext(Local<T, U> task) {
                internal.parent.emit(task.out);
            }

            @Override
            public void onComplete() {
                internal.parent.complete();
            }

            @Override
            public void onError(Throwable e) {
                internal.parent.error(e);
            }
        });
    }

    public Local<T, U> exec(T o){
        return internal.exec(o);
    }

    protected abstract void on(Local<T, U> task);

    @Override
    protected Observable<U> emit(U o){
        Log.f(Tag, this, o);
        if(__observableOn==Scheduler.Self()) {
            __next(o);
        } else {
            __observableOn.dispatch(new ObservableOn<>(this, snapshot(o), null, false));
        }

        return this;
    }

//    protected Operator<T, U> self = this;
//    protected Observable<Task<T, U>> internal = new Observable<Task<T, U>>();
//    protected Scheduler __operatorOn = Scheduler.Self();

//
//    public Operator(){
//        internal.subscribe(new Subscribers.Task<T, U>(){
//            @Override
//            public void onNext(Task<T, U> task){
//                self.emit(task.out);
//            }
//        });
//    }
//
//    protected Task<T, U> out(Task<T, U> task){
//        Log.f(Tag, this, task);
//
//        internal.emit(task);
//
//        return task;
//    }
//
//    public Task<T, U> exec(T o){
//        Task<T, U> task = new Local<>(o, this);
//        if(__operatorOn==Scheduler.Self()){
//            task = on(task);
//        } else {
//            __observableOn.dispatch(task);
//        }
//        return task;
//    }
//
//
//
//    public final Operator<T, U> subscribe(Subscribers.Task<T, U> subscriber){
//        Log.f(Tag, this, subscriber);
//        if(subscriber!=null){
//            synchronized (internal.__observers){
//                if(internal.__observers.add(subscriber)){
//                    subscriber.onSubscribe(internal);
//                } else {
//                    Log.c(Tag, new RuntimeException("internal.__observers.add(subscriber)==false"));
//                }
//            }
//        } else {
//            Log.e(Tag, new RuntimeException("observer==null"));
//        }
//        return this;
//    }
//
//    public final Operator<T, U> unsubscribe(Subscribers.Task<T, U> subscriber){
//        Log.f(Tag, this, subscriber);
//        if(subscriber!=null){
//            synchronized (internal.__observers){
//                if(internal.__observers.remove(subscriber)){
//                    subscriber.onUnsubscribe(internal);
//                } else {
//                    Log.c(Tag, new RuntimeException("internal.__observers.remove(subscriber)==false"));
//                }
//            }
//        } else {
//            Log.e(Tag, new RuntimeException("observer==null"));
//        }
//        return this;
//    }
//
//
//
//    public static <T, U> Operator<T, U> Op(Func<T, U> f){
//        return new Operator<T, U>() {
//            @Override
//            public Task<T, U> on(Task<T, U> task) {
//                task.out = f.call(task.in);
//                return out(task);
//            }
//        };
//    }


}
