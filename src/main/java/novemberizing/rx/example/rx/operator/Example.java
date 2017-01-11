package novemberizing.rx.example.rx.operator;

import i.Observable;
import i.Subscriber;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Example {
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);

        Observable<String> observable = new Observable<String>() {};

        observable.op(
                o->(Integer.parseInt(o)+1)).
                subscribe(new Subscriber<>());

        for(String s : args) {
            observable.update(s);
        }
    }
}
