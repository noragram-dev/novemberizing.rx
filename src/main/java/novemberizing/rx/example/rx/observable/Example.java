package novemberizing.rx.example.rx.observable;

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
        observable.subscribe(new Subscriber<String>(){
            @Override
            public void onNext(String o){
                Log.i("onNext", o);
            }

            @Override
            public void onComplete(){
                Log.i("onComplete","");
            }

            @Override
            public void onError(Throwable e){
                Log.e("onError", e);
            }
        });

        for(String s : args) {
            observable.update(s);
        }
        observable.error(new Exception());
        observable.complete();

    }
}
