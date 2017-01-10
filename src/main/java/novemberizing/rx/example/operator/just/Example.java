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
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Exec(Operator.Just(o-> Log.i("just(f) >",o)), "0");
    }
}
