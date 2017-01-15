package novemberizing.rx.example.operator.completion.port;

import novemberizing.ds.Func;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.example.operator.sync.Run;
import novemberizing.rx.operators.CompletionPort;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 15
 */
public class Example {
    public static void main(String[] args){
        CompletionPort port = new CompletionPort();

        Run<String, Integer> op = new Run<String, Integer>(new Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o);
            }
        });

        port.subscribe(Subscribers.Just("operator.completion.port(task) >"));
        op.subscribe(Subscribers.Just("operator.sync(string) >"));

        for(String s : args){
            port.exec(op.exec(s));
        }

        Scheduler.Local().clear();
    }

}
