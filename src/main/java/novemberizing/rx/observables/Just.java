package novemberizing.rx.observables;

import novemberizing.rx.Observable;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Just<T> extends Observable<T> {
    @Override public Observable<T> emit(T o){ return super.emit(o); }
    @Override public Observable<T> error(Throwable e){ return super.error(e); }
    @Override public Observable<T> complete(){ return super.complete(); }
}