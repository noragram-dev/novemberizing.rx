package novemberizing.rx.operators;

import novemberizing.rx.Observable;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Composer<T, U, Z> extends Operator<T, Z> {
    private static final String Tag = "Composer";

    private novemberizing.ds.func.Pair<T, U, Z> __func;
    private Observable<U> __secondary;
    private final Composer<T, U, Z> __self = this;
    private U __second;
    private T __first;

    protected Observable<U> secondary(){ return __secondary; }

    @Override
    protected void on(Task<T, Z> task, T in) {
        synchronized (__self) {
            try {
                task.next(__func.call(__first = in, __second));
            } catch (Exception e) {
                task.error(e);
            }
            task.complete();
        }
    }

    public Composer(Observable<U> secondary, novemberizing.ds.func.Pair<T, U, Z> f){
        __secondary = secondary;
        __func = f;
        __secondary.subscribe(new Subscriber<U>() {
            @Override
            public void onNext(U o) {
                synchronized (__self) {
                    emit(__func.call(__first, __second = o));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Tag, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(Tag, "");
            }
        });
    }
}
