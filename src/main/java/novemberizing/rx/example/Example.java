package novemberizing.rx.example;

import novemberizing.util.Log;


/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {
    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.FLOW | Log.DEBUG | Log.HEADER);
        Log.disable(Log.FLOW | Log.DEBUG);


        novemberizing.rx.example.observables.Example.main(args);
        novemberizing.rx.example.operators.Example.main(args);
    }
}

