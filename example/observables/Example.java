package novemberizing.rx.example.observables;


import novemberizing.rx.Scheduler;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {
    public static void main(String[] args){

        novemberizing.rx.example.observables.just.Example.main(args);

        Scheduler.Local().clear();
    }
}
