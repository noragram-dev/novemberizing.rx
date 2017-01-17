package novemberizing.ds;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Command implements Executable {
    private static final String Tag = "Command";

    protected Executor __executor = null;
    protected boolean __completed = false;

    @Override
    public void execute(Executor executor) {
        if(__executor!=null){
            Log.d(Tag, this, "__executor!=null");
        }
        __executor = executor;
        execute();
    }

    public void executed() {
        Executor executor = __executor;
        __executor = null;
        executor.executed(this);
    }

    public void complete() {
        Executor executor = __executor;
        __completed = true;
        __executor = null;
        executor.completed(this);
    }

    public abstract void execute();

    @Override
    public boolean completed() { return __completed; }
}
