package i;

import i.operator.Just;
import i.operator.Task;
import novemberizing.util.Debug;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public interface Operator<T, U> extends i.func.Single<T, U> {
    static <T> Just<T> Just(i.func.Single<T, T> f){
        if(f==null) { Debug.On(new RuntimeException("")); }
        return new Just<>(f);
    }

    Task<T> in(Task<T> task);
}
