package novemberizing.rx.ds;

import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Executor implements Cyclable {
    private static final String Tag = "novemberizing.rx.ds.Executor";

    protected final HashSet<Executable> __executables = new HashSet<>();
    protected Queue<Executable> __q;
    protected boolean __running;

    public boolean empty(){
//        Log.f(Tag, "empty", this);
        boolean ret;
        synchronized (__executables){
            ret = __executables.size()==0 && __q.empty();
        }
        return ret;
    }

    synchronized public boolean running(){ return __running; }

    @Override
    public void onecycle(){
//        Log.f(Tag, "onecycle", this);
        if(!running()) {
            synchronized (this) {
                __running = true;
            }
            __q.lock();
            while (__q.size()>0) {
                Executable executable = __q.pop();
                __q.unlock();
                synchronized (__executables) {
                    if (!__executables.add(executable)) {
                        Log.e(Tag, new RuntimeException(""));
                    }
                }
                executable.execute(this);
                __q.lock();
            }
            __q.unlock();
            synchronized (this) {
                __running = false;
            }
        }
    }

    public void executed(Executable executable){
//        Log.f(Tag, "executed", this, executable);
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
            __q.lock();
            __q.push(executable);
            __q.resume(false);
            __q.unlock();
        }
    }

    public void completed(Executable executable){
        Log.f(Tag, "completed", this, executable);
        if(executable!=null) {
            synchronized (__executables) {
                if (!__executables.remove(executable)) {
                    Log.e(Tag, new RuntimeException(""));
                }
            }
        }
    }

    public void dispatch(Executable executable){
        Log.f(Tag, "dispatch", this, executable);
        if(executable!=null){
            __q.lock();
            __q.push(executable);
            __q.resume(false);
            __q.unlock();
        }
    }

    public void clear(){
        Log.f(Tag, "clear", this);
        __q.lock();
        while(!empty()){
            Executable executable = __q.pop();
            __q.unlock();
            if(executable!=null) {
                synchronized (__executables) {
                    if (!__executables.add(executable)) {
                        Log.e(Tag, new RuntimeException(""));
                    }
                }
                executable.execute(this);
            }
            __q.lock();
        }
        __q.unlock();
    }
}
