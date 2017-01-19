package novemberizing.rx.example.operators._switch;

import novemberizing.ds.func.Single;
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


        {
            Switch<String, String> op = new Switch<>(new Single<String, Integer>() {
                @Override
                public Integer call(String o) {
                    return Integer.parseInt(o);
                }
            });
            op._case(1, Block.<String, Integer, String>begin(o -> Integer.parseInt(o) + 10).next(o -> o + 10).ret(o -> (Integer.toString(o))))
                    ._case(2, Block.<String, Integer, String>begin(o -> Integer.parseInt(o) + 10).next(o -> o + 10).ret(o -> (Integer.toString(o))))
                    ._case(3, Block.<String, Integer, String>begin(o -> Integer.parseInt(o) + 10).next(o -> o + 10).ret(o -> (Integer.toString(o))))
                    ._default(Block.<String, Integer, String>begin(o -> Integer.parseInt(o) * 10).next(o -> o + 10).ret(o -> (Integer.toString(o))))
                    .subscribe(Subscribers.Just("op(switch) 1>"));

            op.foreach(args).subscribe(Subscribers.Just("completion(task) 2>"));
        }

        {
            Switch<String, String> op = new Switch<>(new Single<String, Integer>() {
                @Override
                public Integer call(String o) {
                    return Integer.parseInt(o);
                }
            });
            op._case(1, o->Integer.toString((Integer.parseInt(o))))
                    ._case(2, o->Integer.toString((Integer.parseInt(o)+2)))
                    ._case(3, o->Integer.toString((Integer.parseInt(o)+3)))
                    ._default(o->Integer.toString((Integer.parseInt(o)+10)))
                    .subscribe(Subscribers.Just("op(switch) 3>"));

            op.foreach(args).subscribe(Subscribers.Just("completion(task) 4>"));
        }
    }

}
