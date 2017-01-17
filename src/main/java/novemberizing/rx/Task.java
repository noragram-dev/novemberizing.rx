package novemberizing.rx;

import novemberizing.ds.Executable;
import novemberizing.ds.Executor;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Task<T, U> implements Executable {
    private static final String Tag = "Task";

    protected boolean __executed;
    protected Executor __executor;
    protected boolean __completed;
    protected Throwable __exception;
    protected final Observable<Task<T, U>> __completionPort;

    public final T in;
    public U out;

    public Task(T in){
        this.in = in;

        out = null;
        __executor = null;
        __executed = false;
        __completed = false;
        __exception = null;

        __completionPort = null;
    }

    public Task(T in, Observable<Task<T, U>> completionPort){
        this.in = in;

        out = null;
        __executor = null;
        __executed = false;
        __completed = false;
        __exception = null;

        __completionPort = completionPort;
    }

    protected void complete() {
        Executor executor = __executor;

        __completed = true;
        __executor = null;
        if(__completionPort!=null) {
            __completionPort.complete();
        }

        executor.completed(this);
    }

    protected void error(Throwable e) {
        Executor executor = __executor;

        __completed = true;
        __executor = null;
        __exception = e;

        if(__completionPort!=null) {
            __completionPort.error(e);
        }

        executor.completed(this);
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

}
