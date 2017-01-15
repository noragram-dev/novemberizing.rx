package novemberizing.rx.example;

import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {
    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.FLOW | Log.HEADER);
        // Log.disable(Log.FLOW);

        novemberizing.rx.example.observable.Example.main(args);

        novemberizing.rx.example.operator.Example.main(args);

        Scheduler.Local().clear();
    }
}
