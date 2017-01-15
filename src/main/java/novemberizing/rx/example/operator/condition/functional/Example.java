package novemberizing.rx.example.operator.condition.functional;

import novemberizing.ds.Func;
import novemberizing.ds.func.Pair;
import novemberizing.rx.Operator;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.example.operator.sync.Run;
import novemberizing.rx.operators.Condition;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {
    public static void main(String[] args){

        Condition<String, String> op = new Condition<String, String>(new Func<String, Boolean>() {
            @Override
            public Boolean call(String first) {
                return Integer.parseInt(first)%3==0;
            }
        }){

            @Override
            protected void on(Operator.Local<String, String> task) {
                Log.i("", task);
                task.done(task.out = task.in);
            }
        };


        op.subscribe(Subscribers.Just("condition >"));

        for(String s : args){
            op.exec(s);
        }

        Scheduler.Local().clear();
    }
}
