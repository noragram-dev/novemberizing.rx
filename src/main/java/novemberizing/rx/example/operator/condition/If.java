package novemberizing.rx.example.operator.condition;

import i.Operator;
import i.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class If {
    private static int count = 0;
    public static void main(String[] args) {
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Sync(Scheduler.Foreach(
                Operator.If(o -> (Integer.parseInt(o) > 6), Operator.Op((String o) -> (Integer.parseInt(o)),
                        (Integer o) -> 9,
                        (Integer o) -> (Integer.toString(o)),
                        (String o) -> Log.i("if(...) " + (count++) + ">", o))).
                        ElseIf(o -> (Integer.parseInt(o) > 3), Operator.Op((String o) -> (Integer.parseInt(o)),
                                (Integer o) -> 6,
                                (Integer o) -> (Integer.toString(o)),
                                (String o) -> Log.i("if(...) " + (count++) + ">", o))).
                        ElseIf(o -> (Integer.parseInt(o) > 0), Operator.Op((String o) -> (Integer.parseInt(o)),
                                (Integer o) -> 3,
                                (Integer o) -> (Integer.toString(o)),
                                (String o) -> Log.i("if(...) " + (count++) + ">", o))).
                        Else(Operator.Op((String o) -> (Integer.parseInt(o)),
                                (Integer o) -> (o),
                                (Integer o) -> (Integer.toString(o)),
                                (String o) -> Log.i("if(...) " + (count++) + ">", o)))

                , args));
    }
}
