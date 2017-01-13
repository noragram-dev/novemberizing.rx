package novemberizing.ds;

import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 13.
 */
public class Tasks extends novemberizing.ds.Task<Collection<Task>> {
    private static final String Tag = "Tasks";
    protected final LinkedList<Task> __completeds = new LinkedList<>();

    public Tasks(Collection<Task> tasks) {
        super(tasks);
        for(Task o : tasks){
            if(o!=null){
                o.on(new On<Task>(){
                    @Override
                    public void on(Task v) {
                        synchronized (__completeds) {
                            Log.e(Tag, "");
                            __completeds.addLast(v);
                            if(in.size()==__completeds.size()){
                                Log.e(Tag, "completed");
                                completed();
                            }
                        }
                    }
                });
            }
        }
    }

    public Tasks(Collection<Task> tasks, Task<?> parent) {
        super(tasks, parent);
        for(Task o : tasks){
            if(o!=null){
                o.on(new On<Task>(){
                    @Override
                    public void on(Task v) {
                        synchronized (__completeds) {
                            Log.e(Tag, "");
                            __completeds.addLast(v);
                            if(in.size()==__completeds.size()){
                                Log.e(Tag, "completed");
                                completed();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void execute() {
        Log.e(Tag, "");
    }
//
//    @Override
//    public <U extends Task> void onChildCompleted(U o){
//        Log.f(Tag, this, o);
//        __o = o;
//        synchronized (__children){
//            __children.remove(o);
//            if(__children.size()==0){ __done = true; }
//        }
//        if(__done) {
//            completed();
//        } else {
//            executed();
//        }
//    }
}
