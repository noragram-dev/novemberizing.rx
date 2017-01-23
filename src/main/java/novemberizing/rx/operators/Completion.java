package novemberizing.rx.operators;

import novemberizing.rx.Observable;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 23.
 */
public class Completion<T, U, Z> extends Operator<T, Z> {
    private static final String Tag = "Condition";
    private Observable<U> __secondary;
    private novemberizing.ds.func.Pair<T, U, Z> __func;
    private novemberizing.ds.func.Pair<T, U, Boolean> __condition;
    private final Completion<T, U, Z> __self = this;
    private U __second;
    private T __first;
    private boolean __finished;

    public Completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition ,novemberizing.ds.func.Pair<T, U, Z> f){
        __secondary = observable;
        __condition = condition;
        __func = f;
        __finished = false;
        __secondary.subscribe(new Subscriber<U>() {
            @Override
            public void onNext(U o) {
                synchronized (__self) {
                    __finished = false;
                    __second = o;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Tag, e.getMessage());
                synchronized (__self) {
                    __finished = false;
                }
            }

            @Override
            public void onComplete() {
                Log.d(Tag, "");
                synchronized (__self) {
                    __finished = true;
                    if(__condition==null || __condition.call(__first, __second)){
                        emit(__func.call(__first, __second));
                    }
                }
            }
        });
    }

    protected Observable<U> secondary(){ return __secondary; }

    @Override
    protected void on(Task<T, Z> task, T in) {
        synchronized (__self) {
            __first = in;
            try {
                if(__finished && (__condition==null || __condition.call(__first , __second))) {
                    task.next(__func.call(__first = in, __second));
                }
            } catch (Exception e) {
                task.error(e);
            }
            task.complete();
        }
    }
}