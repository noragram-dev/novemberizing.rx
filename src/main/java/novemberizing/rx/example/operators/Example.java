package novemberizing.rx.example.operators;

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
            public Task<String, Integer> exec(String o) {
                return out(o, Integer.parseInt(o) + 10);
            }
        };

        op.subscribe(Subscribers.Just("operator.just(string) >"));

        for(String s : args){
            op.exec(s);
        }



//        op.exec
//

//
//        op.complete();
    }
}
