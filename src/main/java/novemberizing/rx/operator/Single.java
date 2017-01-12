package novemberizing.rx.operator;

import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Single<T> extends Operator<T, T> {

    private static final String Tag = "Single";

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER);
        Scheduler.Exec(Single(o->o+10), 1);
    }
}
