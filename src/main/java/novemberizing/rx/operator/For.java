package novemberizing.rx.operator;

import com.google.gson.annotations.Expose;
import novemberizing.rx.Operator;
import novemberizing.rx.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public class For<T, V> extends Operator<T, T> {
    public class Local<T, V> extends novemberizing.rx.Local<T> {
        private static final String Tag = "Local";
        @Expose public T in;
        @Expose public V local;
        @Expose public Object out;

        public Local(T in){
            super(in);
            Log.f(Tag, "");
            this.local = null;
            this.out = null;
        }
    }

    protected Operator.Func<T, V> __initializer;
    protected novemberizing.rx.func.Pair<T, V, Boolean> __condition;
    protected novemberizing.rx.func.Pair<T, V, V> __op;
    protected Operator<T, ?> __block;

    public For(Operator.Func<T, V> initializer, novemberizing.rx.func.Pair<T, V, Boolean> condition, novemberizing.rx.func.Pair<T, V, V> op, novemberizing.rx.func.Single<T, ?> block){
        __initializer = initializer;
        __condition = condition;
        __op = op;
        __block = Op(block);
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        ((Local<T, V>) task.v).local = __initializer!=null ? __initializer.call(o) : null;
        if(__condition.call(o, ((Local<T, V>) task.v).local)){
            task.set(o, o);
            __iterate(task);
            task.down(__block, o);
            return null;
        } else {
            task.out(o);
        }
        return null;
    }

    @Override
    protected void out(Task<T> task, Object o){
        if (!task.done()) {
            ((Local<T, V>) task.v).local = __op!=null ? __op.call((T) o, ((Local<T, V>) task.v).local) : ((Local<T, V>) task.v).local;
            if(__condition.call((T) o, ((Local<T, V>) task.v).local)){
                task.set(task.v.in, o);
                task.down(__block, (T) o);
            } else {
                super.out(task, o);
            }
        }
    }

    @Override
    public novemberizing.rx.Local<T> declare(Task<T> task){
        return new Local<>(task.i());
    }
}
