package novemberizing.ds;

import com.google.gson.annotations.Expose;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Command implements Executable {
    private static final String Tag = "novemberizing.ds.Command";

    @Expose protected Executor __executor;

    synchronized private boolean set(Executor executor){
        Log.f(Tag, "set", this, executor);
        if(__executor==null && executor!=null) {
            __executor = executor;
            return true;
        }
        return false;
    }

    @Override
    public void execute(Executor executor) {
        Log.f(Tag, "execute", this, executor);
        if(set(executor)){
            execute();
        } else {
            Log.e(Tag, this, executor);
        }
    }

    @Override
    public void executed(){
        Log.f(Tag, "executed", this);
        if(__executor!=null){
            __executor.executed(this);
            __executor = null;
        }
    }

    @Override
    public void completed(){
        Log.f(Tag, "completed", this);
        if(__executor!=null){
            __executor.completed(this);
            __executor = null;
        }
    }
}
