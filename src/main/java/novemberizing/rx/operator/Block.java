package novemberizing.rx.operator;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Block<T, U> extends Operator<T, U> {

    protected Operator<T, ?> __front;

    public <C> Block(novemberizing.rx.func.Single<T, C> f){
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
