package novemberizing.rx.example.operator.condition.observal;

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
//        Run<String, Integer> sync = new Run<String, Integer>(new Func<String, Integer>() {
//            @Override
//            public Integer call(String o) {
//                return Integer.parseInt(o);
//            }
//        });
//
//        Condition<String, String> op = new Condition<String, String>(sync, new Pair<String, Integer, Boolean>() {
//            @Override
//            public Boolean call(String first, Integer second) {
//                return second!=null && second%3==0;
//            }
//        }) {
//            @Override
//            protected void on(Operator.Local<String, String> task) {
//                Log.i("", task);
//                task.done(task.out = task.in);
//            }
//        };
//
//        op.exec("hello");
//
//        op.subscribe(Subscribers.Just("condition >"));
//
//        for(String s : args){
//            sync.exec(s);
//        }
//
//        Scheduler.Local().clear();
    }
}
