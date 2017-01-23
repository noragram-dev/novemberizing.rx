package novemberizing.rx;

import novemberizing.ds.Executable;
import novemberizing.ds.Executor;
import novemberizing.ds.func.Single;
import novemberizing.rx.operators.Completion;
import novemberizing.rx.operators.Composer;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Sync;
import novemberizing.util.Log;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Task<T, Z> implements Executable {
    private static final String Tag = "Task";

    protected boolean __executed;
    protected Executor __executor;
    protected boolean __completed;
    protected Throwable __exception;
    protected Observable<Z> __completionPort;
    protected Replayer<Z> __replayer;

    public final T in;
    public Z out;

    public Task(T in){
        this.in = in;

        out = null;
        __executor = null;
        __executed = false;
        __completed = false;
        __exception = null;

        __replayer = new Replayer<>(Infinite);
    }

    protected void complete() {
        if(!__completed) {
            Executor executor = __executor;

            __completed = true;
            __executor = null;

            if (__completionPort != null) {
                __completionPort.complete();
            } else {
                __replayer.complete(out);
            }

            if (executor != null) {
                executor.completed(this);
            } else {
                Log.d(Tag, "executor is null");
            }
        } else {
            Log.d(Tag, "completed");
        }
    }

    protected abstract void execute();

    synchronized protected boolean executed(){ return __executed; }

    @Override
    public void execute(Executor executor) {
        synchronized (this){
            if(__executed){
                Log.d(Tag, "__executed==true");
                return;
            }
            __executed = true;
        }
        if(__executor!=null){
            Log.d(Tag, this, "__executor!=null");
        }
        __executor = executor;
        execute();
    }

    @Override
    public boolean completed() { return __completed; }

    public void next(Z o) {

        if (__completionPort != null) {
            __completionPort.emit(o);
        } else {
            __replayer.add(o);
        }
    }

    public void error(Throwable e) {
        if (__completionPort != null) {
            __completionPort.error(e);
        } else {
            __replayer.error(e);
        }
    }

    public Observable<Z> subscribe(Observer<Z> observer){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return __completionPort.subscribe(observer);
    }

    public Observable<Z> unsubscribe(Observer<Z> observer){ return __completionPort!=null ? __completionPort.unsubscribe(observer) : null; }
    public Observable<Z> unsubscribe(){ return __completionPort!=null ? __completionPort.unsubscribe() : null; }


    public <U> Sync<Z, U> sync(Single<Z, U> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Sync<Z, U>) __completionPort.subscribe(Operator.Sync(f));
    }
    public <U> Sync<Z, U> sync(novemberizing.ds.on.Pair<Operator.Task<Z, U>, Z> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Sync<Z, U>) __completionPort.subscribe(Operator.Sync(f));
    }

    public <U, V> Composer<Z, U, V> compose(Observable<U> secondary, novemberizing.ds.func.Pair<Z, U, V> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Composer<Z, U, V>) __completionPort.subscribe(Operator.Composer(secondary,f));
    }

    public <U, V> Condition<Z, U, V> condition(Observable<U> observable, novemberizing.ds.func.Pair<Z, U, Boolean> condition , novemberizing.ds.func.Pair<Z, U, V> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Condition<Z, U, V>) __completionPort.subscribe(Operator.Condition(observable,condition,f));
    }

    public <U, V> Completion<Z, U, V> completion(Observable<U> observable, novemberizing.ds.func.Pair<Z, U, Boolean> condition , novemberizing.ds.func.Pair<Z, U, V> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Completion<Z, U, V>) __completionPort.subscribe(Operator.Completion(observable,condition,f));
    }

    public <U> Operator<Z, U> condition(Single<Z, Boolean> condition, Single<Z, U> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return __completionPort.subscribe(Operator.Condition(condition, f));
    }

    public Observable<Z> replay(int limit){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        if(limit==0){
            __completionPort.__replayer = null;
        } else if(__replayer==null){
            __completionPort.__replayer = new Replayer<>(limit);
        } else {
            __completionPort.__replayer.limit(limit);
        }
        return __completionPort;
    }

}
