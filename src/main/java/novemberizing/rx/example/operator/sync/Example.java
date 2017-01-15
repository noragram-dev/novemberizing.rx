package novemberizing.rx.example.operator.sync;

import novemberizing.ds.Func;
import novemberizing.rx.*;


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

        op.subscribe(Subscribers.Just("operator.sync(string)"));

        for(String s : args){
            op.exec(s);
        }

        Scheduler.Local().clear();
    }
}
