package novemberizing.ds;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 13.
 */
public class Task<T, U> {
    @Expose public final T in;
    @Expose public U out;

    public Task(T o){
        in = o;
    }
}
