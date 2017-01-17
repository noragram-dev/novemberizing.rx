package novemberizing.rx.example.observables.just;


import novemberizing.rx.Observable;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.observables.Just;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {
    public static void main(String[] args){
        Just<String> observable = new Just<>();

        observable.replay(Infinite).subscribe(Subscribers.Just("observable.just(string) 1>"));

        Observable.foreach(observable, args).subscribe(Subscribers.Just("completion(task) 2>"));

        observable.subscribe(Subscribers.Just("observable.just(string) 3>"));

        Observable.foreach(observable, args).subscribe(Subscribers.Just("completion(task) 4>"));

        Observable.error(observable, new RuntimeException("message")).subscribe(Subscribers.Just("completion(error) 5>"));

        Observable.complete(observable).subscribe(Subscribers.Just("completion(complete) 6>"));

        Observable.foreach(observable, args).subscribe(Subscribers.Just("completion(task) 7>"));

        Scheduler.Local().clear();
    }
}
