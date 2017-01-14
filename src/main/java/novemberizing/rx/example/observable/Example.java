package novemberizing.rx.example.observable;

import novemberizing.rx.Observable;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.FLOW | Log.HEADER);

        Observable<String> observable = new Observable<String>() {};

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String o) {
                Log.i("observable >", o);
            }

            @Override
            public void onComplete() { Log.i("observable >", "complete"); }

            @Override
            public void onError(Throwable e) {
                Log.i("observable(error) >", this, e);
            }
        });

        for(String s : args){
            observable.next(s);
        }
        observable.complete();
    }
}
