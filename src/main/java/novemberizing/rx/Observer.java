package novemberizing.rx;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public interface Observer<T> {



    Scheduler observeOn();
    Observer<T> observeOn(Scheduler scheduler);

    void onNext(T o);
    void onError(Throwable e);
    void onComplete();

    void onSubscribe(Observable<T> observable);
    void onUnsubscribe(Observable<T> observable);
}
