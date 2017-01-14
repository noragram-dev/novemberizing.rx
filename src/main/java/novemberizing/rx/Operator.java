package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Operator<T, U> extends Observable<Task<T, U>> {

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
    }

    protected Scheduler __operatorOn = Scheduler.Self();
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

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag, this, task);

        next(task);

        return task;
    }

    public Task<T, U> exec(T o){
        Task<T, U> task = new Local<>(o, this);
        if(__operatorOn==Scheduler.Self()){
            task = on(task);
        } else {
            __observableOn.dispatch(task);
        }
        return task;
    }

    public abstract Task<T, U> on(Task<T, U> task);

    public static <T, U> Operator<T, U> Op(Func<T, U> f){
        return new Operator<T, U>() {
            @Override
            public Task<T, U> on(Task<T, U> task) {
                task.out = f.call(task.in);
                return out(task);
            }
        };
    }
}
