package novemberizing.rx.operators;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Until<T> extends While<T> {
    public Until(Block.Op<T, T> block, Single<T, Boolean> condition) {
        super(condition, block);
    }

    public Until(Single<T, T> f, Single<T, Boolean> condition) {
        super(condition, f);
    }

    @Override
    protected void on(Operator.Task<T, T> task, T in) {
        execute(__block, task, in);
    }
}
