package novemberizing.rx.example.operator.sync;

import i.Operator;
import i.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Foreach(Operator.Sync(o -> Log.i("sync(f) >", o)), args);
    }
}
