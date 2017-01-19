package novemberizing.rx.example.operators.sync;


import novemberizing.ds.func.Single;
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
        Operator<String, String> op = new Order<>(new Single<String, String>() {
            @Override
            public String call(String o) {
                return o + "th";
            }
        });

        op.replay(Infinite).subscribe(Subscribers.Just("operator.sync(string) 1>"));

        op.exec("-1").subscribe(Subscribers.Just("completion(task) 2>"));

        op.subscribe(Subscribers.Just("operator.sync(string) 3>"));

        op.foreach(args).subscribe(Subscribers.Just("completion(task) 4>"));

        Scheduler.Local().clear();
    }
}