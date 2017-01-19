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
public class Req<T> {
    private static final String Tag = "Req";
    private Task<Req<T>, T> __task;

    boolean executed(){ return __task!=null && __task.executed(); }

    boolean completed(){return __task!=null && __task.completed(); }

    public Observable<T> subscribe(Observer<T> observer){
        Log.e(Tag, "not requested");
        return __task!=null ? __task.subscribe(observer) : null;
    }


    public Observable<T> unsubscribe(Observer<T> observer){

        return __task!=null ? __task.unsubscribe(observer) : null;
    }
    public Observable<T> unsubscribe(){
        return __task!=null ? __task.unsubscribe() : null;
    }


    public <U> Sync<T, U> sync(Single<T, U> f){
        return __task!=null ? __task.sync(f) : null;
    }
    public <U> Sync<T, U> sync(novemberizing.ds.on.Pair<Operator.Task<T, U>, T> f){
        return __task!=null ? __task.sync(f) : null;
    }

    public <U, V> Composer<T, U, V> compose(Observable<U> secondary, novemberizing.ds.func.Pair<T, U, V> f){
        return __task!=null ? __task.compose(secondary, f) : null;
    }

    public <U, V> Condition<T, U, V> condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition , novemberizing.ds.func.Pair<T, U, V> f){
        return __task!=null ? __task.condition(observable, condition, f) : null;
    }

    public <U> Operator<T, U> condition(Single<T, Boolean> condition, Single<T, U> f){
        return __task!=null ? __task.condition(condition, f) : null;
    }

    public Observable<T> replay(int limit){
        return __task!=null ? __task.replay(limit) : null;
    }
}
