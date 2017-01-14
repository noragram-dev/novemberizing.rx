package novemberizing.rx;

import novemberizing.ds.Executable;
import novemberizing.ds.Executor;
import novemberizing.rx.schedulers.Local;


/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Scheduler extends Executor {
    public static Scheduler Self(){ return Local.Get(); }
    public static Scheduler Local(){ return Local.Get(); }
    public static Scheduler New(){ return Local.Get(); }



}
