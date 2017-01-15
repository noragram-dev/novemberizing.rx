package novemberizing.rx.example.operator.async;

import novemberizing.ds.Func;
import novemberizing.rx.*;
import novemberizing.rx.observables.Just;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {

    public static void main(String[] args){
        Run<String, Integer> op = new Run<String, Integer>(new Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o);
            }
        });

        op.subscribe(Subscribers.Just("operator.async(string)"));

        for(String s : args){
            op.exec(s);
        }

        Scheduler.Local().clear();
    }
}
