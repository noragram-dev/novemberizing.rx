package novemberizing.rx.example.operators._if;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;
import novemberizing.rx.operators.Block;
import novemberizing.rx.operators.If;
import novemberizing.rx.operators.Switch;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 19.
 */
public class Example {
    public static void main(String[] args){
        {
            Operator<String, Integer> op = new Operator<String, Integer>() {
                @Override
                protected void on(Task<String, Integer> task, String in) {
                    task.next(Integer.parseInt(in) + 10);
                    task.complete();
                    ;
                }
            };

            op.subscribe(Subscribers.Just("op() 1>"));

            op.next(
                    Operator.If(o -> o > 16, Block.<Integer, Integer, String>begin(o -> o + 10).next(o -> o + 40).ret(o -> Integer.toString(o))).
                            _elseif(o -> o > 13, Block.<Integer, Integer, String>begin(o -> o + 10).next(o -> o + 30).ret(o -> Integer.toString(o))).
                            _elseif(o -> o > 10, Block.<Integer, Integer, String>begin(o -> o + 10).next(o -> o + 20).ret(o -> Integer.toString(o))).
                            _else(Block.<Integer, Integer, String>begin(o -> o + 10).next(o -> o + 10).ret(o -> Integer.toString(o)))
            ).subscribe(Subscribers.Just("if(block) 2>"));



            op.foreach(args);
        }

        {
            Operator<String, Integer> op = new Operator<String, Integer>() {
                @Override
                protected void on(Task<String, Integer> task, String in) {
                    task.next(Integer.parseInt(in) + 10);
                    task.complete();
                    ;
                }
            };

            op.subscribe(Subscribers.Just("op() 3>"));

            op.next(
                    Operator.If((Integer o) -> o >16, o->"9").
                            _elseif(o -> o > 13, o->"6").
                            _elseif(o -> o > 10, o->"3").
                            _else(o->"-1")
            ).subscribe(Subscribers.Just("if(func) 4>"));



            op.foreach(args);
        }

    }

}
