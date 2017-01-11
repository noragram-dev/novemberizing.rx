package i.operator;

import i.Operator;
import i.Task;
import novemberizing.util.Log;

import java.util.HashMap;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Switch<T, U> extends Operator<T, U> {
    protected Operator.Func<T, Integer> __hashing;
    protected Operator<T, ?> __default;
    protected HashMap<Integer,Operator<T, ?>> __cases = new HashMap<>();


    public Switch(Operator.Func<T, Integer> f){
        __hashing = f;
    }

    public <V> Switch<T, U> Case(int i, i.func.Single<T, V> f){
        if(__default!=null){
            Log.e("", new RuntimeException("__default!=null"));
        } else {
            if (__cases.get(i) != null) {
                Log.w("", new RuntimeException("__cases.get(i)!=null"));
            }
            __cases.put(i, Op(f));
        }
        return this;
    }

    public <V> Switch<T, U> Default(i.func.Single<T, V> f){
        if(__default==null){
            __default = Op(f);
        } else {
            Log.e("", new RuntimeException("__default!=null"));
        }
        return this;
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        Integer i = __hashing.call(o);
        Operator<T, ?> op = __cases.get(i);
        if(op!=null){
            task.set(o, null);
            __iterate(task);
            task.down(op, o);
            return null;
        }
        task.set(o, null);
        __iterate(task);
        if(__default!=null) {
            task.down(__default, o);
        } else {
            task.out(null);
        }
        return null;
    }
}
