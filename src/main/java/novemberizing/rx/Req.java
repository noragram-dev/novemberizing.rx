package novemberizing.rx;


import novemberizing.ds.func.Single;
import novemberizing.rx.operators.Composer;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Sync;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Req<T, Z> {
    private static final String Tag = "Req";
    private Task<T, Z> __task;

    boolean executed(){ return __task!=null && __task.executed(); }

    boolean completed(){return __task!=null && __task.completed(); }

    public Observable<Z> subscribe(Observer<Z> observer){
        Log.e(Tag, "not requested");
        return __task!=null ? __task.subscribe(observer) : null;
    }


    public Observable<Z> unsubscribe(Observer<Z> observer){

        return __task!=null ? __task.unsubscribe(observer) : null;
    }
    public Observable<Z> unsubscribe(){
        return __task!=null ? __task.unsubscribe() : null;
    }


    public <U> Sync<Z, U> sync(Single<Z, U> f){
        return __task!=null ? __task.sync(f) : null;
    }
    public <U> Sync<Z, U> sync(novemberizing.ds.on.Pair<Operator.Task<Z, U>, Z> f){
        return __task!=null ? __task.sync(f) : null;
    }

    public <U, V> Composer<Z, U, V> compose(Observable<U> secondary, novemberizing.ds.func.Pair<Z, U, V> f){
        return __task!=null ? __task.compose(secondary, f) : null;
    }

    public <U, V> Condition<Z, U, V> condition(Observable<U> observable, novemberizing.ds.func.Pair<Z, U, Boolean> condition , novemberizing.ds.func.Pair<Z, U, V> f){
        return __task!=null ? __task.condition(observable, condition, f) : null;
    }

    public <U> Operator<Z, U> condition(Single<Z, Boolean> condition, Single<Z, U> f){
        return __task!=null ? __task.condition(condition, f) : null;
    }

    public Observable<Z> replay(int limit){
        return __task!=null ? __task.replay(limit) : null;
    }
}
