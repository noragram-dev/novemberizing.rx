package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Func;
import novemberizing.rx.operators.Condition;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Task<T, Z> extends novemberizing.ds.Task {
    private static final String Tag = "Task";
    @Expose public final T in;
    @Expose public Z out;
    @Expose protected Observable<Task<T, Z>> __observable = null;

    public Task(T in) {
        this.in = in;
        this.out = null;
    }

    protected Task(T in, Z out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() {
        complete();
    }

    public Observable<Task<T, Z>> append(Observer<Task<T, Z>> observer){
        if(__observable==null){
            __observable = new Observable<>();
        }
        __observable.subscribe(observer);
        return __observable;
    }

    public <U> Operator<Task<T, Z>, U> append(Operator<Task<T, Z>, U> op){
        if(__observable==null){
            __observable = new Observable<>();
        }
        return __observable.append(op);
    }

    public <OUT> Condition<Task<T, Z>, OUT> condition(Func<Task<T, Z>, Boolean> condition, Func<Task<T, Z>, OUT> f){
        if(__observable==null){
            __observable = new Observable<>();
        }
        return (Condition<Task<T, Z>, OUT>) __observable.subscribe(Operator.Condition(condition, f));
    }

    public <U, OUT> Condition<T, OUT> condition(Observable<U> observable, novemberizing.ds.func.Pair<Task<T, Z>, U, Boolean> condition, novemberizing.ds.func.Pair<Task<T, Z>, U, OUT> f){
        if(__observable==null){
            __observable = new Observable<>();
        }
        return (Condition<T, OUT>) __observable.subscribe(Operator.Condition(observable, condition, f));
    }

    @Override
    protected void executed() {
        Log.f(Tag, this);
        super.executed();
        if(__observable!=null) {
            __observable.emit(this);
        }

    }

    @Override
    protected void complete() {
        Log.f(Tag, this);

        super.complete();
        if(__observable!=null) {
            __observable.complete(this);
        }
    }
}
