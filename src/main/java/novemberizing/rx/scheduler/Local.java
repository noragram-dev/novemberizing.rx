package novemberizing.rx.scheduler;

import novemberizing.ds.Queue;
import novemberizing.ds.Executable;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Local extends Scheduler {
    private static final String Tag = "novemberizing.rx.scheduler.Local";

    private static ThreadLocal<Local> __schedulers = new ThreadLocal<>();

    public static Scheduler Get(){
        Log.f(Tag, "Get");
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
        __q = new Queue<>();
//        Scheduler.Set(this);
    }

    public void dispatch(Executable executable){
        Log.f(Tag, "dispatch", this, executable);
        super.dispatch(executable);
        onecycle();
    }

    @Override
    public void executed(Executable executable){
        Log.f(Tag, "executed", this, executable);
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
            __q.lock();
            __q.front(executable);
            __q.resume(false);
            __q.unlock();
        }
//        onecycle();
    }

//    @Override
//    public void completed(Executable executable){
//        super.completed(executable);
//        onecycle();
//    }

}
