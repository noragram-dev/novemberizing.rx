package novemberizing.rx.example.operators.chain;

import novemberizing.ds.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;
import novemberizing.rx.Task;
import novemberizing.rx.example.operators.async.Run;
import novemberizing.rx.operators.Sync;

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

        op.subscribe(Subscribers.Just("operator.chain(1) >"))
                .append(new Sync<>(new Run<>(new Func<Integer,Integer>(){
                    @Override
                    public Integer call(Integer o) {
                        return o+10;
                    }
                }))).subscribe(Subscribers.Just("operator.chain(2) >"))
                .append(new Func<Integer, String>(){
                    @Override
                    public String call(Integer o) {
                        return Integer.toString(o) + "th";
                    }
                }).subscribe(Subscribers.Just("operator.chain(3) >"));

        ////        op.append(new Sync<>(new Run<>(new Func<Integer, Integer>() {
////            @Override
////            public Integer call(Integer o) {
////                return o+10;
////            }
////        })))

        for(String s : args){
            op.exec(s);
        }
//
//        Operator<String, Integer> op = new Operator<String, Integer>() {
//            @Override
//            protected Task<String, Integer> on(Task<String, Integer> task) {
//                task.out = Integer.parseInt(task.in) + 10;
//                return task;
//            }
//        };
//
//        op.subscribe(Subscribers.Just("operator.chain(1) >"));
//
// .subscribe(Subscribers.Just("operator.chain(1) >")).
////                append(new Func<Integer, String>(){
////            @Override
////            public String call(Integer o) {
////                return Integer.toString(o) + "th";
////            }
////        }).subscribe(Subscribers.Just("operator.chain(3) >"));
//
//        for(String s : args){
//            op.exec(s);
//        }
//
//        /**
//         * op.next(op)
//         * op.append(op)
//         *
//         */
//
////        op.next();
//
//        // op.next(...)
//        // op.next(...);
//
//        /**
//         * op.onDone(...)
//         * op.subscribe(...)
//         * op.
//         */
//
////        op.next();
//
//        // op.subscribe(Operator.Op((Integer o)->o+1))
//
//        // novemberizing.rx.example.operators.completion.port.Example.main(args);
    }
}
