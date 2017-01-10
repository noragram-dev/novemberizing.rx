package novemberizing.rx.example.operator.just;

import i.Operator;
import i.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {
    private static int count = 0;
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Foreach(Operator.Just(o-> Log.i("just(f) " + (count++) + ">",o)), args);
    }
}
