package novemberizing.rx.example.operators._switch;


import novemberizing.ds.func.Single;
import novemberizing.rx.Subscriber;
import novemberizing.rx.Subscribers;
import novemberizing.rx.operators.Block;
import novemberizing.rx.operators.Switch;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Example {
    public static void main(String[] args){
//        Switch<String, Integer> op = new Switch<>(new Func<String, Integer>() {
//            @Override
//            public Integer call(String o) {
//                return Integer.parseInt(o);
//            }
//        });
//        Block<String, Integer> block = new Block<>();
//        op._case(1, block.start(o->(Integer.parseInt(o) + 1)).end(o->o))
//                ._case(2, block.start(o->(Integer.parseInt(o) + 1)).end(o->o))
//                ._case(3, block.start(o->(Integer.parseInt(o) + 1)).end(o->o))
//                ._default(block.start(o->(Integer.parseInt(o) + 10)).end(o->o))
//                .subscribe(Subscribers.Just("switch >"));

//        Block.
//                Begin(o->(Integer.parseInt(o) + 1).
//
//
//                .append(...)
//
//            End()


//        op.foreach(args).subscribe(Subscribers.Just("completion(task) >"));
//        op._case(1, block.start().append(new Func<String, Integer>(){
//            @Override
//            public Integer call(String o) {
//                return null;
//            }
//        });
    }

}