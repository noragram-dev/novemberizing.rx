package novemberizing.rx.schedulers;

import novemberizing.ds.ConditionalList;
import novemberizing.ds.Executable;
import novemberizing.ds.Factory;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Local extends Scheduler {

    private static Factory<Local> __factory = null;
    private static ThreadLocal<Scheduler> __schedulers = new ThreadLocal<>();

    public static void Set(Factory<Local> factory){ __factory = factory; }

    public static Scheduler Get(){
        Scheduler scheduler = __schedulers.get();
        if(scheduler==null){
            scheduler = __factory!=null ? __factory.create() : new Local();
            if(scheduler==null){
                scheduler = new Local();
            }
            __schedulers.set(scheduler);
        }
        return scheduler;
    }

    public Local(){
        __q = new ConditionalList<>();
    }

    @Override
    public void executed(Executable executable) {
        super.executed(executable);
        onecycle();
    }

    @Override
    public void completed(Executable executable) {
        super.completed(executable);
        onecycle();
    }

    @Override
    public void dispatch(Executable executable) {
        super.dispatch(executable);
        onecycle();
    }

    @Override
    public void dispatch(Executable executable, Executable... executables) {
        super.dispatch(executable, executables);
        onecycle();
    }

    @Override
    public void dispatch(Executable[] executables) {
        super.dispatch(executables);
        onecycle();
    }
}
