package novemberizing.rx.scheduler;

import novemberizing.rx.Executable;
import novemberizing.rx.Scheduler;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Local extends Scheduler {

    private static ThreadLocal<Local> __schedulers = new ThreadLocal<>();

    public static Local Get(){
        Local scheduler = __schedulers.get();
        if(scheduler==null){
            scheduler = new Local();
            __schedulers.set(scheduler);
        }
        return scheduler;
    }

    public Local(){
        __q = new novemberizing.ds.queue<>();
    }

    public void dispatch(Executable executable){
        super.dispatch(executable);
        onecycle();
    }

    public void dispatch(Executable executable, Executable... executables){
        super.dispatch(executable, executables);
        onecycle();
    }

    public void dispatch(Executable[] executables){
        super.dispatch(executables);
        onecycle();
    }

    @Override
    public void executed(Executable executable){
        if(executable!=null){
            del(executable);
            __q.lock();
            __q.front(executable);
            __q.resume(false);
            __q.unlock();
        }
        onecycle();
    }

    @Override
    public void completed(Executable executable){
        if(executable!=null){
            del(executable);
        }
        onecycle();
    }
}
