package novemberizing.rx;

import novemberizing.ds.Executor;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Operator<T, U> implements Executor {

    protected abstract void in();
    protected abstract void on();
    protected abstract void out();
}
