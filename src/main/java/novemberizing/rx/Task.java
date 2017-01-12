package novemberizing.rx;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */

public class Task<T, U> extends novemberizing.ds.Task<T, U> {
    @Expose protected Operator<T, U> __op;

    public Task(T o, Operator<T, U> op) {
        super(o);
        __op = op;
    }
}
