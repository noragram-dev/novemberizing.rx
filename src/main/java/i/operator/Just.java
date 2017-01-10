package i.operator;

import novemberizing.util.Debug;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Just<T> extends Operator<T, T> {
    private static final String Tag = "Just";

    protected i.func.Single<T, T> __func;

    public Just(){
        Log.f(Tag, "");
        __func = null;
    }

    public Just(i.func.Single<T, T> f){
        Log.f(Tag, "");
        __func = f;
    }

    @Override
    protected Task<T> __on(Task<T> task) {
        Log.f(Tag, "");
        task.v.out = call(task.v.in);
        task.it(Iteration.Out);
        return __out(task);
    }

    @Override
    public T call(T in){
        Log.f(Tag, "");
        if(__func==null){ Debug.On(new RuntimeException("")); }
        return __func!=null ? __func.call(in) : in;
    }
}
