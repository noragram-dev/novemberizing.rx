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
    private novemberizing.ds.tuple.Single<U> __second;
    private T __first;

    public Completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition ,novemberizing.ds.func.Pair<T, U, Z> f){
        __secondary = observable;
        __condition = condition;
        __func = f;
        __secondary.subscribe(new Subscriber<U>() {
            @Override
            public void onNext(U o) {
                synchronized (__self) {
                    __second = new novemberizing.ds.tuple.Single<>(o);
                    if(__completed && (__condition==null || __condition.call(__first, __second.first))){
                        emit(__func.call(__first, __second.first));
                    }
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

    protected Observable<U> secondary(){ return __secondary; }

    @Override
    protected void on(Task<T, Z> task, T in) {
        synchronized (__self) {
            __first = in;

        }
        task.complete();
    }

    @Override
    public void onNext(T o) {
        super.onNext(o);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onComplete(){
        Log.e(Tag, "= 1 =");
        if(__second!=null) {
            Log.e(Tag, "= 2 =");
            if(__condition==null || __condition.call(__first, __second.first)){
                Log.e(Tag, "= 3 =");
                emit(__func.call(__first, __second.first));
            }
        }
        super.onComplete();
    }
}