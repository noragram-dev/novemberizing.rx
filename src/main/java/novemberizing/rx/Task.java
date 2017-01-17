package novemberizing.rx;

import novemberizing.ds.Executable;
import novemberizing.ds.Executor;
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

            __replayer.complete(out);

            if (__completionPort != null) {
                __completionPort.complete();
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
        __replayer.add(o);
        if (__completionPort != null) {
            __completionPort.emit(o);
        }
    }

    public void error(Throwable e) {
        __replayer.error(e);
        if (__completionPort != null) {
            __completionPort.error(e);
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
}
