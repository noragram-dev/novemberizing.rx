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

        Observable.Foreach(observable, args).subscribe(Subscribers.Just("completion(task) 2>"));

        observable.subscribe(Subscribers.Just("observable.just(string) 3>"));

        Observable.Foreach(observable, args).subscribe(Subscribers.Just("completion(task) 4>"));

        Observable.Error(observable, new RuntimeException("message")).subscribe(Subscribers.Just("completion(error) 5>"));

        Observable.Complete(observable).subscribe(Subscribers.Just("completion(complete) 6>"));

        Observable.Foreach(observable, args).subscribe(Subscribers.Just("completion(task) 7>"));

        Scheduler.Local().clear();
    }
}
