package novemberizing.rx.example.operator.switchcase;


import i.Operator;
import i.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Example {
    private static int count = 0;
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Sync(Scheduler.Foreach(
                Operator.Switch((String o)->(Integer.parseInt(o))).
                        Case(9,Operator.Op((String o)->(Integer.parseInt(o)),
                                (Integer o)->-9,
                                (Integer o)->(Integer.toString(o)),
                                (String o)->Log.i("if(...) " + (count++) + ">", o))).
                        Case(0,Operator.Op((String o)->(Integer.parseInt(o)),
                                (Integer o)->-1,
                                (Integer o)->(Integer.toString(o)),
                                (String o)->Log.i("if(...) " + (count++) + ">", o))).
                        Default(Operator.Op((String o)->(Integer.parseInt(o)),
                                (Integer o)->(o+10),
                                (Integer o)->(Integer.toString(o)),
                                (String o)->Log.i("if(...) " + (count++) + ">", o)))

                ,args));
    }
}
