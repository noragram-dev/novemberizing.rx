package novemberizing.rx.example.operators._for;

import novemberizing.rx.Operator;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.operators.Block;
import novemberizing.rx.operators.For;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Example {
    public static void main(String[] args){
        {
            For<String, Integer> _for = Operator.For(()->0,(o, i)->i<10,i->i+1, o->o+o);
            _for.foreach(args).subscribe(new Subscribers.Just<String>("for(...) 1>"){
                int count = 0;
                @Override
                public void onNext(String s){
                    Log.i(tag(), "length: " + (++count) + " : " + s.length());
                }
            });
        }

        {
            For<String, Integer> _for = Operator.For(()->0,(o, i)->i<10,i->i+1, Operator.Op(o->o+o+o));
            _for.foreach(args).subscribe(new Subscribers.Just<String>("for(...) 2>"){
                int count = 0;
                @Override
                public void onNext(String s){
                    Log.i(tag(), "length: " + (++count) + " : " + s.length());
                }
            });
        }

        {
            For<String, Integer> _for = Operator.For(()->0,(o, i)->i<10,i->i+1, Operator.Block(o->o+o+o+o));
            _for.foreach(args).subscribe(new Subscribers.Just<String>("for(...) 3>"){
                int count = 0;
                @Override
                public void onNext(String s){
                    Log.i(tag(), "length: " + (++count) + " : " + s.length());
                }
            });
        }
//
//        {
//            While<Integer> _while = Operator.While(o -> o < 0, Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o));
//            _while.exec(0).subscribe(Subscribers.Just("while(block) 3>"));
//
//            Until<Integer> _dowhile = Operator.Until(Block.<Integer, Integer, Integer>begin(o -> o + 1).ret(o -> o), o -> o < 0);
//            _dowhile.exec(0).subscribe(Subscribers.Just("dowhile(block) 4>"));
//        }
//
//        {
//            While<Integer> _while = Operator.While(o -> o < 0, o->o+1);
//            _while.exec(0).subscribe(Subscribers.Just("while(block) 5>"));
//
//            Until<Integer> _dowhile = Operator.Until(o->o+1, o -> o < 0);
//            _dowhile.exec(0).subscribe(Subscribers.Just("dowhile(block) 6>"));
//        }

        Scheduler.Local().clear();
    }

}
