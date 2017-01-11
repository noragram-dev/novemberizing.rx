package i;

import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Subscriber<T> implements Observer<T> {
    private static final String Tag = "Subscriber";

    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn;

    @Override
    synchronized public void onNext(T o) {
        Log.i(Tag, o);
    }

    @Override
    synchronized public void onError(Throwable e) {
        Log.i(Tag, e);
    }

    @Override
    synchronized public void onComplete() {
        Log.i(Tag, "onComplete");
    }

    @Override
    synchronized public void onSubscribe(Observable<T> observable) {
        synchronized (__observables) {
            __observables.add(observable);
        }
    }

    @Override
    synchronized public void onUnsubscribe(Observable<T> observable) {
        synchronized (__observables) {
            __observables.add(observable);
        }
    }

    @Override
    public Scheduler observeOn() { return __observeOn; }
}
