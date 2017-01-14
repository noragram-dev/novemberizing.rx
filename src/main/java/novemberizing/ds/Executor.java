package novemberizing.ds;

import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings({"WeakerAccess", "unused", "DanglingJavadoc"})
public abstract class Executor implements Cyclable {
    private static final String Tag = "Executor";

    protected Queue<Executable> __q;
    protected final HashSet<Executable> __executables = new HashSet<>();
    protected boolean __running = false;

    @Override
    public void onecycle() {
        if(!__running) {
            __q.lock();
            __running = true;
            while (__q.size() > 0) {
                Executable executable = __q.pop();
                __q.unlock();
                synchronized (__executables) {
                    if (!__executables.add(executable)) {
                        Log.e(Tag, new RuntimeException("!__executables.add(executable)"));
                    }
                }
                executable.execute(this);
                __q.lock();
            }
            __running = false;
            __q.unlock();
        }
    }

    public void clear(){
        int remain;
        __q.lock();
        do {
            while(__q.size()>0){
                Executable executable = __q.pop();
                __q.unlock();
                synchronized (__executables){
                    if(!__executables.add(executable)){
                        Log.e(Tag, new RuntimeException("!__executables.add(executable)"));
                    }
                }
                executable.execute(this);
                __q.lock();
            }
            synchronized (__executables) {
                remain = __executables.size();
            }
        } while(remain>0);
        __q.unlock();
    }

    public void executed(Executable executable) {
        if(executable!=null){
            synchronized (__executables){
                if(!__executables.remove(executable)){
                    Log.e(Tag, new RuntimeException("!__executables.remove(executable)"));
                }
            }
            dispatch(executable);
        }
    }

    public void completed(Executable executable) {
        if(executable!=null){
            synchronized (__executables){
                if(!__executables.remove(executable)){
                    Log.e(Tag, new RuntimeException("!__executables.remove(executable)"));
                }
            }
            dispatch(executable);
        }
    }

    public void dispatch(Executable executable){
        __q.lock();
        __q.push(executable);
        __q.resume(false);
        __q.unlock();
    }

    public void dispatch(Executable executable, Executable... executables){
        __q.lock();
        __q.push(executable);
        for(Executable e : executables){
            __q.push(e);
        }
        __q.resume(true);
        __q.unlock();
    }

    public void dispatch(Executable[] executables){
        __q.lock();
        for(Executable e : executables){
            __q.push(e);
        }
        __q.resume(true);
        __q.unlock();
    }
}
