package novemberizing.rx.example.operators.append;

import novemberizing.rx.*;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {



    public static void main(String[] args){
        Operator<String, String> op = Operator.Op(new Func<String, String>() {
            @Override
            public String call(String o) {
                return o + "th";
            }
        });

        op.replay(Infinite).subscribe(Subscribers.Just("operator.append(string) 1>"));

        op.exec("-1").subscribe(Subscribers.Just("completion(task) 2>"));

        op.subscribe(Subscribers.Just("operator.append(string) 3>"));

        op.foreach(args).subscribe(Subscribers.Just("completion(task) 4>"));

        Scheduler.Local().clear();
    }
}
