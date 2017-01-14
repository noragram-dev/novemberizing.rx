package novemberizing.rx.example.operators.sync;

import novemberizing.ds.Func;
import novemberizing.rx.Subscribers;
import novemberizing.rx.example.operators.async.Run;
import novemberizing.rx.operators.Sync;


/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("Convert2Lambda")
public class Example {

    public static void main(String[] args){
        Sync<String, Integer> op = new Sync<>(new Run<>(new Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return o!=null ? Integer.parseInt(o) + 10 : null;
            }
        }));

        op.subscribe(Subscribers.Just("operator.sync(string,integer) >"));

        for(String s : args){
            op.exec(s);
        }
    }
}

