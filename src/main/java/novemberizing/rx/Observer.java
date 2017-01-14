package novemberizing.rx;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public interface Observer<T> {
    void onNext(T o);
    void onComplete();
    void onError(Throwable e);

    void onSubscribe(Observable<T> observer);
    void onUnsubscribe(Observable<T> observer);

    Scheduler observeOn();
}
