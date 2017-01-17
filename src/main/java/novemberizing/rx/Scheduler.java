package novemberizing.rx;

import novemberizing.ds.Executable;
import novemberizing.ds.Executor;
import novemberizing.rx.schedulers.Local;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Scheduler implements Executor {
    private static ThreadLocal<Scheduler> __scheduler = new ThreadLocal<>();

    public static Scheduler New(){ return Local.Get(); }

    public static Scheduler Local(){ return Local.Get(); }

    protected static void Self(Scheduler self){
        if(self!=null) {
            Scheduler scheduler = __scheduler.get();
            if (scheduler == null) {
                __scheduler.set(scheduler);
            } else if (scheduler != self) {
                __scheduler.set(self);
            }
        } else {
            __scheduler.remove();
        }
    }

    public static Scheduler Self(){
        Scheduler scheduler = __scheduler.get();
        if(scheduler==null){
            scheduler = Scheduler.New();
        }
        return scheduler;
    }
}
