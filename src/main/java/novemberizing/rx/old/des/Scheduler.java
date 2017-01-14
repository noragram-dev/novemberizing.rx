package novemberizing.rx.old.des;

import novemberizing.rx.ds.Executor;
import novemberizing.rx.old.scheduler.Local;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("DanglingJavadoc")
public abstract class Scheduler extends Executor {
    private static final String Tag = "Scheduler";

    public static Scheduler Self(){ return Local(); }

    public static Scheduler Local(){ return Local.Get(); }



}
