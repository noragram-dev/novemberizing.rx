package novemberizing.rx.example.operators._while;

import novemberizing.rx.Operator;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.operators.Block;
import novemberizing.rx.operators.Until;
import novemberizing.rx.operators.While;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Example {
    public static void main(String[] args){
        {
            While<Integer> _while = Operator.While(o -> o < 10, Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o));
            _while.exec(0).subscribe(Subscribers.Just("while(block) 1>"));

            Until<Integer> _dowhile = Operator.Until(Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o), o -> o < 10);
            _dowhile.exec(0).subscribe(Subscribers.Just("dowhile(block) 2>"));
        }

        {
            While<Integer> _while = Operator.While(o -> o < 0, Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o));
            _while.exec(0).subscribe(Subscribers.Just("while(block) 3>"));

            Until<Integer> _dowhile = Operator.Until(Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o), o -> o < 0);
            _dowhile.exec(0).subscribe(Subscribers.Just("dowhile(block) 4>"));
        }

        {
            While<Integer> _while = Operator.While(o -> o < 0, o->o+1);
            _while.exec(0).subscribe(Subscribers.Just("while(block) 5>"));

            Until<Integer> _dowhile = Operator.Until(o->o+1, o -> o < 0);
            _dowhile.exec(0).subscribe(Subscribers.Just("dowhile(block) 6>"));
        }

        Scheduler.Local().clear();
    }

}
