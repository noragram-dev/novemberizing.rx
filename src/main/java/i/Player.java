package i;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public interface Player<T> {
    void onNext(T o);
    void onError(Throwable e);
    void onComplete();
}
