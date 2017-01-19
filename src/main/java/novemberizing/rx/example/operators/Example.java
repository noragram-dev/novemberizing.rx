package novemberizing.rx.example.operators;


import novemberizing.rx.Scheduler;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Example {
    public static void main(String[] args){

//        novemberizing.rx.example.operators.append.Example.main(args);
//        novemberizing.rx.example.operators.sync.Example.main(args);
//        novemberizing.rx.example.operators.async.Example.main(args);
//        novemberizing.rx.example.operators.composer.Example.main(args);
//        novemberizing.rx.example.operators.chain.Example.main(args);
//        novemberizing.rx.example.operators.block.Example.main(args);
//        novemberizing.rx.example.operators._switch.Example.main(args);
//        novemberizing.rx.example.operators._if.Example.main(args);
        novemberizing.rx.example.operators._while.Example.main(args);
        Scheduler.Local().clear();
    }
}
