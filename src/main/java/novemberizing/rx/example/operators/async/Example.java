package novemberizing.rx.example.operators.async;


import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {

    public static void main(String[] args){
        Operator<String, String> op = new Unorder<>(new Func<String, String>() {
            @Override
            public String call(String o) {
                return o + "th";
            }
        });

        op.replay(Infinite).subscribe(Subscribers.Just("operator.async(string) 1>"));

        op.exec("-1").subscribe(Subscribers.Just("completion(task) 2>"));

        op.subscribe(Subscribers.Just("operator.async(string) 3>"));

        op.foreach(args).subscribe(Subscribers.Just("completion(task) 4>"));

        Scheduler.Local().clear();
    }
}