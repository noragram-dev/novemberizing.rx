package novemberizing.rx.example.observables.just;


import novemberizing.rx.Observable;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.observables.Just;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {
    public static void main(String[] args){
        Just<String> observable = new Just<>();

        observable.subscribe(Subscribers.Just("observable.just(string) >"));

        Observable.foreach(observable, args).subscribe(Subscribers.Just("completion(task) >"));

        Observable.error(observable, new RuntimeException("")).subscribe(Subscribers.Just("completion(error) >"));

        Observable.complete(observable).subscribe(Subscribers.Just("completion(complete) >"));

        Scheduler.Local().clear();
    }
}
