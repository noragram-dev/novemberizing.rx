package novemberizing.rx.example.operator.async.sync;

import i.Operator;
import i.Scheduler;
import i.Task;
import novemberizing.util.Log;

import java.util.Collection;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {

    private static int count = 0;

    public static void main(String[] args){
        Log.disable(Log.HEADER | Log.FLOW);
        Log.depth(3);

        Collection<Task<String>> tasks = Scheduler.Foreach(Sync.New(1000, o -> Log.i("async(sync(f)) " + (count++) + ">", o)), args);

        for(Task<String> o : tasks){
            while(!o.done()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}