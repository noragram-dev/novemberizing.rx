package novemberizing.rx.operator.just;

import i.Operator;
import i.operator.Just;
import i.operator.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Example {
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);

        Just<String> just = Operator.Just(o->(o + "th"));

        for(String s : args){
            Log.i("debug", just.in(new Task<>(s)));
        }

    }
}
