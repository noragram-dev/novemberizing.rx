package novemberizing.rx.example.observable;

import novemberizing.rx.Observable;
import novemberizing.rx.Subscriber;
import novemberizing.rx.observables.Just;
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

        Just<String> observable = Observable.Just();

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String o) {
                Log.i("observable.just(string) >", o);
            }

            @Override
            public void onComplete() { Log.i("observable.just(string) >", "complete"); }

            @Override
            public void onError(Throwable e) {
                Log.e("observable.just(string) >", "error: " + e.getMessage());
            }
        });


        for(String s : args){
            observable.next(s);
        }

        observable.complete();
    }
}
