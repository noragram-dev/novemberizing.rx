package novemberizing.rx.example;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {
    public static void main(String[] args){
        Log.depth(3);
//        Log.disable(Log.FLOW | Log.HEADER);

        novemberizing.rx.example.observable.Example.main(args);
    }
}
