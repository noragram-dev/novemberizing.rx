package novemberizing.rx.scheduler;

import novemberizing.rx.Executable;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Local extends Scheduler {
    private static final String Tag = "Local";

    private static ThreadLocal<Local> __schedulers = new ThreadLocal<>();

    public static Scheduler Get(){
        Local scheduler = __schedulers.get();
        if(scheduler==null){
            scheduler = new Local();
            __schedulers.set(scheduler);
        }
        return scheduler;
    }

    public void finalize() throws Throwable {

//        Scheduler.Del(this);
        clear();
        super.finalize();
    }

    public Local(){
//        Scheduler.Set(this);
    }

    public void dispatch(Executable executable){
        super.dispatch(executable);
        onecycle();
    }

    @Override
    public void executed(Executable executable){
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
            __q.front(executable);
        }
    }

}
