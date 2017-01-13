package novemberizing.rx;

import novemberizing.ds.Executor;
import novemberizing.rx.scheduler.Local;

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
