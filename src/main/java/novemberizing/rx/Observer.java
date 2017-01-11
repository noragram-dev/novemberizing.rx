package novemberizing.rx;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public interface Observer<T> extends Player<T> {
    void onSubscribe(Observable<T> observable);
    void onUnsubscribe(Observable<T> observable);
    Scheduler observeOn();
}
