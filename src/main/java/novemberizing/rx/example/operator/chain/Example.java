package novemberizing.rx.example.operator.chain;

import novemberizing.ds.Func;
import novemberizing.rx.Observable;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Subscribers;
import novemberizing.rx.example.operator.sync.Run;
import novemberizing.rx.observables.Just;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 15
 */
public class Example {

    public static void main(String[] args){
        Just<String> observable = Observable.Just();

        observable.
                subscribe(Subscribers.Just("observal.just(string)")).
                append(new Func<String, Integer>() {
                    @Override
                    public Integer call(String o) {
                        return Integer.parseInt(o) + 10;
                    }
                }).
                subscribe(Subscribers.Just("chain.append(f)")).
                append(new Run<Integer, Integer>(new Func<Integer, Integer>() {
                    @Override
                    public Integer call(Integer o) {
                        return o + 10;
                    }
                })).
                subscribe(Subscribers.Just("f.append(sync(f))")).
                append(new Func<Integer, Integer>() {
                    @Override
                    public Integer call(Integer o) {
                        return o + 10;
                    }
                }).
                subscribe(Subscribers.Just("sync.append(f)")).
                append(new Run<Integer, Integer>(new Func<Integer, Integer>() {
                    @Override
                    public Integer call(Integer o) {
                        return o + 10;
                    }
                })).
                subscribe(Subscribers.Just("f.append(async(f))"));


        for(String s : args){
            observable.emit(s);
        }

        Scheduler.Local().clear();
    }
}