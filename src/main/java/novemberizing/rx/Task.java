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
    private Task<T, Z> self= this;
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

    synchronized public Observable<Task<T, Z>> append(Observer<Task<T, Z>> observer){
        if(__observable==null){
            __observable = new Observable<>();
        }

        __observable.subscribe(observer);

        if(completed()){
            Log.e(Tag, this);
            __observable.emit(this);
        }

        return __observable;
    }

    synchronized public <U> Operator<Task<T, Z>, U> append(Operator<Task<T, Z>, U> op){
        if(__observable==null){
            __observable = new Observable<>();
        }

        Operator<Task<T, Z>, U> p =  __observable.append(op);
        __observable.subscribe(op);

        if(completed()){
            Log.e(Tag, this);
            __observable.emit(this);
        }

        return p;
    }

    synchronized public <OUT> Condition<Task<T, Z>, OUT> condition(Func<Task<T, Z>, Boolean> condition, Func<Task<T, Z>, OUT> f){
        if(__observable==null){
            __observable = new Observable<>();
        }

        Condition<Task<T, Z>, OUT> c = ((Condition<Task<T, Z>, OUT>) __observable.subscribe(Operator.Condition(condition, f)));
        __observable.subscribe(c);

        if(completed()){
            Log.e(Tag, this);
            __observable.emit(this);
        }


        return c;
    }

    synchronized public <U, OUT> Condition<Task<T, Z>, OUT> condition(Observable<U> observable, novemberizing.ds.func.Pair<Task<T, Z>, U, Boolean> condition, novemberizing.ds.func.Pair<Task<T, Z>, U, OUT> f){
        if(__observable==null){
            __observable = new Observable<>();
        }

        Condition<Task<T, Z>, OUT> c = ((Condition<Task<T, Z>, OUT>) __observable.subscribe(Operator.Condition(observable, condition, f)));
        __observable.subscribe(c);

        if(completed()){
            Log.e(Tag, this);
            __observable.emit(this);
        }



        return c;
    }

    @Override
    protected void executed() {
        Log.f(Tag, this);
        super.executed();
        synchronized(this) {
            Log.e(Tag, this);
            if (__observable != null) {
                __observable.emit(this);
            }
        }

    }

    @Override
    protected void complete() {
        Log.f(Tag, this);

        super.complete();
        synchronized(this) {
            Log.e(Tag, this);
            if (__observable != null) {
                __observable.complete(this);
            }
        }
    }
}
