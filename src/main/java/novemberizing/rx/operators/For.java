package novemberizing.rx.operators;

import novemberizing.rx.Operator;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class For<T, U, Z> extends Operator<T, Z> {

    protected novemberizing.ds.func.Pair<T, Z, U> __initializer;
    protected novemberizing.ds.func.Triple<T, U, Z, Boolean> __condition;
    protected novemberizing.ds.func.Triple<T, U, Z, U> __internal;
    protected Block<T, Z> __external;

    public For(novemberizing.ds.func.Pair<T, Z, U> initializer,
               novemberizing.ds.func.Triple<T, U, Z, Boolean> condition,
               novemberizing.ds.func.Triple<T, U, Z, U> internal,
               Block<T, Z> external){
        __initializer = initializer;
        __condition = condition;
        __internal = internal;
        __external = external;
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        /**
         * initializer op
         * condition op
         * internal op
         * external op
         */
    }
}
