package novemberizing.ds;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public abstract class Command implements Executable {
    private static final String Tag = "Command";

    protected Executor __executor = null;
    protected boolean __completed = false;
    protected CompletionPort __completionPort = null;

    public abstract void execute();

    @Override
    public void execute(Executor executor) {
        Log.f(Tag, this, executor);

        synchronized (this){
            if(__executor!=null){
                Log.e(Tag, new RuntimeException("__executor!=null"));
            }
            __executor = executor;
        }
        execute();
    }

    protected void executed() {
        Log.f(Tag, this);

        Executor executor;
        synchronized (this) {
            executor = __executor;
            __executor = null;
            if (executor == null) {
                Log.e(Tag, new RuntimeException("__executor==null"));
            }
        }
        if(executor!=null) {
            executor.executed(this);
        }
    }

    protected void complete() {
        Log.f(Tag, this);

        Executor executor;
        synchronized (this) {
            __completed = true;
            executor = __executor;
            __executor = null;
            if (executor == null) {
                Log.e(Tag, new RuntimeException("__executor==null"));
            }
            if(__completionPort!=null){
                __completionPort.dispatch(this);
            }
        }

        if(executor!=null){
            executor.completed(this);
        }
    }

    @Override
    synchronized public boolean completed(){ return __completed; }

    @Override
    synchronized public void set(CompletionPort completionPort){
        Log.f(Tag, this, completionPort);
        __completionPort = completionPort;
        if(__completed){
            __completionPort.dispatch(this);
        }
    }
}
