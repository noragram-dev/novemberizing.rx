package novemberizing.rx;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Command implements Executable {
    private static final String Tag = "Command";

    protected Executor __executor;

    private boolean set(Executor executor){
        if(__executor==null && executor!=null) {
            __executor = executor;
            return true;
        }
        return false;
    }

    @Override
    public void execute(Executor executor) {
        if(set(executor)){
            execute();
        } else {
            Log.e(Tag, this, executor);
        }
    }
}
