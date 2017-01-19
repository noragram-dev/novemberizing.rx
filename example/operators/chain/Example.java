package novemberizing.rx.example.operators.chain;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.example.operators.async.Unorder;
import novemberizing.rx.example.operators.sync.Order;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {

    public static void main(String[] args){
        Operator<String, String> order = new Order<>(new Single<String, String>() {
            @Override
            public String call(String o) {
                return o + "th";
            }
        });

        order.replay(Infinite).subscribe(Subscribers.Just("operator.sync(string) 1>"));

        order.exec("-1").subscribe(Subscribers.Just("completion(task) 2>"));

        order.subscribe(Subscribers.Just("operator.sync(string) 3>"));

        order.foreach(args).subscribe(Subscribers.Just("completion(task) 4>"));

        Scheduler.Local().clear();

        Operator<String, String> unorder = new Unorder<>(new Single<String, String>() {
            @Override
            public String call(String o) {
                return o + "th";
            }
        });

        Operator<String, String> chain = unorder.next(order);
        chain.subscribe(Subscribers.Just("operator.chain(string) 5>"));

        unorder.exec("-1").subscribe(Subscribers.Just("completion(task) 6>"));

        unorder.subscribe(Subscribers.Just("operator.chain(string) 7>"));

        unorder.foreach(args).subscribe(Subscribers.Just("completion(task) 8>"));

        Scheduler.Local().clear();
    }
}