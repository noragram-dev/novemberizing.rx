package novemberizing.rx.example.observable;

import novemberizing.rx.Observable;
import novemberizing.rx.Scheduler;
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

        Observable<String> observable = new Observable<String>();

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String o) {
                Log.i("observable(string) >", o);
            }

            @Override
            public void onComplete() { Log.i("observable(string) >", "complete"); }

            @Override
            public void onError(Throwable e) {
                Log.e("observable(string) >", "error: " + e.getMessage());
            }
        });



        for(String s : args){
            Observable.emit(observable, s);
        }

        Observable.error(observable, new RuntimeException("error"));

        Observable.complete(observable);

        Scheduler.Local().clear();

        novemberizing.rx.example.observable.just.Example.main(args);
    }
}
