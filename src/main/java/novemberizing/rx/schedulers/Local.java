package novemberizing.rx.schedulers;

import novemberizing.ds.Executable;
import novemberizing.ds.Queue;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

import java.util.concurrent.Callable;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Local extends Scheduler {
    private static final String Tag = "Local";

    private static Callable<Local> __factory = null;

    public static void factory(Callable<Local> factory){
        synchronized (Local.class){
            __factory = factory;
        }
    }

    private static ThreadLocal<Local> __schedulers = new ThreadLocal<>();

    public static Local Get(){
        Local scheduler = __schedulers.get();
        if(scheduler==null){
            try {
                scheduler = __factory == null ? new Local() : __factory.call();
            } catch(Exception e){
                Log.e(Tag, e);
                scheduler = new Local();
            }
            __schedulers.set(scheduler);
        }
        return scheduler;
    }

    public Local(){
        __q = new Queue<>();
    }

    @Override
    public void executed(Executable executable) {
        if(executable!=null){
            synchronized (__executables){
                if(!__executables.remove(executable)){
                    Log.e(Tag, new RuntimeException("!__executables.remove(executable)"));
                }
            }
            __q.lock();
            __q.front(executable);
            __q.resume(false);
            __q.unlock();
            onecycle();
        }
    }

    @Override
    public void completed(Executable executable) {
        if(executable!=null){
            synchronized (__executables){
                if(!__executables.remove(executable)){
                    Log.e(Tag, new RuntimeException("!__executables.remove(executable)"));
                }
            }
            onecycle();
        }
    }

    @Override
    public void dispatch(Executable executable){
        super.dispatch(executable);
        onecycle();
    }

    @Override
    public void dispatch(Executable executable, Executable... executables){
        super.dispatch(executable, executables);
        onecycle();
    }

    @Override
    public void dispatch(Executable[] executables){
        super.dispatch(executables);
        onecycle();
    }
}
