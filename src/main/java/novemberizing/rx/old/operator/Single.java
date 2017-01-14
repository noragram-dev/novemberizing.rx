package novemberizing.rx.old.operator;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Single<T> extends Operator<T, T> {
    private static final String Tag = "novemberizing.rx.old.operator.Single";

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER | Log.FLOW);
//        Exec(Single(o->o+10), 1).on(o->Log.i("",o));
    }
}
