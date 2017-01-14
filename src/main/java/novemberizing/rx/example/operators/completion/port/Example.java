package novemberizing.rx.example.operators.completion.port;

import novemberizing.ds.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;
import novemberizing.rx.Task;
import novemberizing.rx.example.operators.async.Run;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("Convert2Lambda")
public class Example {
    public static void main(String[] args){
        Run<String, Integer> op = new Run<>(new Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o) + 10;
            }
        });

        op.subscribe(Subscribers.Just("operator.async(string,integer) >"));

        for(String s : args){
            op.exec(s);
        }
    }
}