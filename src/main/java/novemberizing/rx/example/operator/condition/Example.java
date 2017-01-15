package novemberizing.rx.example.operator.condition;

import novemberizing.rx.Scheduler;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Example {
    public static void main(String[] args){
        novemberizing.rx.example.operator.condition.observal.Example.main(args);

        novemberizing.rx.example.operator.condition.functional.Example.main(args);

        Scheduler.Local().clear();
    }
}
