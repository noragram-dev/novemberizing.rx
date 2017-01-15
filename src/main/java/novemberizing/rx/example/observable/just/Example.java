package novemberizing.rx.example.observable.just;

import novemberizing.rx.Observable;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscriber;
import novemberizing.rx.Subscribers;
import novemberizing.rx.observables.Just;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {

    public static void main(String[] args){
        Just<String> observable = Observable.Just();

        observable.subscribe(Subscribers.Just("observable.just(string)"));

        for(String s : args){
            observable.emit(s);
        }

        observable.complete();

        Scheduler.Local().clear();
    }
}
