package novemberizing.rx.example.operator;

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
        Operator<String, Integer> op = new Operator<String, Integer>(){
            @Override
            protected void on(Local<String, Integer> task) {
                task.done((Integer.parseInt(task.in) + 10));
            }
        };

        op.subscribe(Subscribers.Just("operator(string)"));

        for(String s : args){
            op.exec(s);
        }

        Scheduler.Local().clear();

        novemberizing.rx.example.operator.async.Example.main(args);
    }
}
