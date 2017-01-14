package novemberizing.rx.example.operators.just;

import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;
import novemberizing.rx.Task;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {
    public static void main(String[] args){
        Operator<String, Integer> op = new Operator<String, Integer>() {

            @Override
            public Task<String, Integer> on(Task<String, Integer> task) {
                task.out = Integer.parseInt(task.in) + 10;
                return out(task);
            }
        };

        op.subscribe(Subscribers.Just("operator.just(string) >"));

        for(String s : args){
            op.exec(s);
        }
    }
}

