package novemberizing.rx.example.operators.chain;

import novemberizing.rx.Operator;
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
                return task;
            }
        };

        /**
         * op.onDone(...)
         * op.subscribe(...)
         * op.
         */

//        op.next();

        // op.subscribe(Operator.Op((Integer o)->o+1))

        // novemberizing.rx.example.operators.completion.port.Example.main(args);
    }
}
