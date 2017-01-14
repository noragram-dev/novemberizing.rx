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
    }

    protected Task<T, U> out(T in, U out){
        return out(new Task<>(in, out));
    }

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag, this, task);

        next(task);

        return task;
    }

    public abstract Task<T, U> exec(T o);

    public static <T, U> Operator<T, U> Op(Func<T, U> f){
        return new Operator<T, U>() {
            @Override
            public Task<T, U> exec(T o) {
                return out(o, f.call(o));
            }
        };
    }
}
