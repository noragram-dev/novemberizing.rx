package i.operator;

import i.Operator;
import i.Task;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Block<T, U> extends Operator<T, U> {

    protected Operator<T, ?> __front;

    public Block(i.func.Single<T, U> f){
        __front = Op(f);
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        task.set(o, null);
        __iterate(task);
        task.down(__front, o);
        return null;
    }
}
