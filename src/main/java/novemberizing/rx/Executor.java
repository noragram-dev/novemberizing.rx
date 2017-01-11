package novemberizing.rx;

import java.util.HashSet;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Executor implements Cyclable {
    protected final HashSet<Executable> __executables = new HashSet<>();
    protected novemberizing.ds.queue<Executable> __q;
    protected boolean __running = false;

    synchronized public boolean running(){ return __running; }
    synchronized protected void running(boolean v){ __running = v; }

    synchronized protected void add(Executable executable){ __executables.add(executable); }
    synchronized protected void del(Executable executable){ __executables.remove(executable); }

    public void finalize(){
        terminate();
    }

    public void dispatch(Executable executable){
        __q.lock();
        __q.back(executable);
        __q.resume(false);
        __q.unlock();
    }

    public void dispatch(Executable executable, Executable... executables){
        __q.lock();
        __q.back(executable);
        for(Executable o : executables){
            __q.back(o);
        }
        __q.resume(true);
        __q.unlock();
    }

    public void dispatch(Executable[] executables){
        __q.lock();
        for(Executable o : executables){
            __q.back(o);
        }
        __q.resume(true);
        __q.unlock();
    }

    public void executed(Executable executable){
        if(executable!=null){
            del(executable);
            __q.lock();
            __q.back(executable);
            __q.resume(false);
            __q.unlock();
        }
    }

    public void completed(Executable executable){
        if(executable!=null){
            del(executable);
        }
    }

    public void terminate(){
        int remain;
        do {
            __q.lock();
            while (__q.size() > 0) {
                Executable executable = __q.front();
                if (executable != null) {
                    __q.unlock();
                    add(executable);
                    executable.execute(this);
                    __q.lock();
                }
            }
            synchronized (__executables){
                remain = __executables.size();
                remain = remain==0 ? __q.size() : 0;
            }
            __q.unlock();
        } while(remain>0);
    }

    @Override
    public void onecycle() {
        if (!running()) {
            running(true);
            __q.lock();
            while(__q.size()>0){
                Executable executable = __q.pop();
                if(executable!=null) {
                    __q.unlock();
                    add(executable);
                    executable.execute(this);
                    __q.lock();
                }
            }
            __q.unlock();
            running(false);
        }
    }
}
