package novemberizing.rx;

import novemberizing.ds.Queue;
import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Executor implements Cyclable {
    private static final String Tag = "Executor";

    protected final HashSet<Executable> __executables = new HashSet<>();
    protected Queue<Executable> __q;
    protected boolean __running;

    public boolean empty(){
        boolean ret;
        synchronized (__executables){
            ret = __executables.size()==0 && __q.empty();
        }
        return ret;
    }

    synchronized public boolean running(){ return __running; }

    @Override
    public void onecycle(){
        if(!running()) {
            synchronized (this) {
                __running = true;
            }
            while (!__q.empty()) {
                Executable executable = __q.pop();
                synchronized (__executables) {
                    if (!__executables.add(executable)) {
                        Log.e(Tag, new RuntimeException(""));
                    }
                }
                executable.execute(this);
            }
            synchronized (this) {
                __running = false;
            }
        }
    }

    public void executed(Executable executable){
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
            __q.push(executable);
        }
    }

    public void completed(Executable executable){
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
        }
    }

    public void dispatch(Executable executable){
        if(executable!=null){
            __q.push(executable);
        }
    }

    public void clear(){
        while(!empty()){
            Executable executable = __q.pop();
            synchronized (__executables){
                if(!__executables.add(executable)){
                    Log.e(Tag, new RuntimeException(""));
                }
            }
            executable.execute(this);
        }
    }
}
