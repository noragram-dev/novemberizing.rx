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

        protected Internal(Operator<T, U> p){
            this.parent = p;
            subscribe(new Subscriber<Local<T, U>>() {
                @Override
                public void onNext(Local<T, U> task) {
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
    }

    private Operator<T, U> self = this;
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
        initialize();
    }

    protected Internal<T, U> initialize(){
        return internal = new Internal<>(this);
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



    public final Operator<T, U> subscribe(Subscribers.Task<T, U> subscriber){
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

    public final Operator<T, U> unsubscribe(Subscribers.Task<T, U> subscriber){
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


    public static <T, U> Operator<T, U> Op(Func<T, U> f){
        return new Operator<T, U>() {
            @Override
            public void on(Local<T, U> task) {
                task.done(task.out = f.call(task.in));
            }
        };
    }

}
