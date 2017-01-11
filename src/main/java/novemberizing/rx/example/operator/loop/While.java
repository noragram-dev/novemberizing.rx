package novemberizing.rx.example.operator.loop;

import i.Operator;
import i.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class While {
    private static int count = 0;
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Scheduler.Sync(Scheduler.Foreach(Operator.Op((String o)->(Integer.parseInt(o)),
                Operator.While((Integer o)->o<9,(Integer o)->(o+1)),
                (Integer o)->(Integer.toString(o)),
                (String o)->Log.i("while(op) " + (count++) + ">", o)),args));
    }
}
