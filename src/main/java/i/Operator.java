package i;

import i.operator.Just;
import i.operator.Sync;
import i.operator.Task;
import novemberizing.util.Debug;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public interface Operator<T, U> extends i.func.Single<T, U> {

    interface Func<T, U> extends i.func.Single<T, U> {}

    static <T> Operator<T, T> Just(Func<T, T> f){
        if(f instanceof Operator){
            return (Operator<T, T>) f;
        } else if(f==null){ Debug.On(new RuntimeException("")); }
        return new Just<>(f);
    }

    static <T, U> Operator<T, U> Sync(Func<T, U> f){
        if(f instanceof Operator){
            return (Operator<T, U>) f;
        } else if(f==null){ Debug.On(new RuntimeException("")); }
        return new Sync<>(f);
    }

    Task<T> in(Task<T> task);
}
