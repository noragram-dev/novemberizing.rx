package novemberizing.rx.example.operator.completion.port;

import i.Operator;
import i.Scheduler;
import i.Task;
import novemberizing.rx.example.operator.async.sync.Sync;
import novemberizing.util.Log;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {
    private static int count = 0;
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        LinkedList<String> collection = new LinkedList<>();
        Collections.addAll(collection, args);
        Task task = Scheduler.Exec(Operator.CompletionPort(Sync.New(1000, o -> Log.i("async(sync(f)) " + (count++) + ">", o))), collection);
        while(!task.done()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
