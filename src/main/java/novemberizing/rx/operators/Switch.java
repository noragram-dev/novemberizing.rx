package novemberizing.rx.operators;

import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.util.Log;

import java.util.HashMap;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Switch<T> extends Operator<T, T> {
    private static final String Tag = "Switch";
    private Func<T, Integer> __hash;
    private Operator<T, ?> __default;
    private HashMap<Integer, Operator<T, ?>> __operators = new HashMap<>();

    public Switch(Func<T, Integer> hash){
        __hash = hash;
    }

    public Switch<T> _case(int index, Operator<T, ?> op){
        if(__default!=null){
            Log.d(Tag, "__default!=null");
        }
        if(__operators.get(index)!=null){
            Log.d(Tag, "__default!=null");
        }
        __operators.put(index, op);
        return this;
    }

    public Switch<T> _default(Operator<T, ?> op){
        if(__default!=null){
            Log.d(Tag, "__default!=null");
        }
        __default = op;
        return this;
    }

    @Override
    protected void on(Task<T, T> task, T in) {
        try {
            Integer i = __hash.call(in);
            if (i != null) {
                Operator<T, ?>  op =__operators.get(i);
                if(op!=null){
                    op.exec(in);
                } else {
                    __default.exec(in);
                }
            } else {
                __default.exec(in);
            }
        } catch (Exception e){
            task.error(e);
        }
        task.complete();
    }
}
