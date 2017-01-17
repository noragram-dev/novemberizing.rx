package novemberizing.rx.schedulers;

import novemberizing.ds.ConditionalList;
import novemberizing.ds.Executable;
import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
class Scheduler extends novemberizing.rx.Scheduler {
    private static final String Tag = "Scheduler";

    protected ConditionalList<Executable> __q;
    protected final HashSet<Executable> __executables = new HashSet<>();
    protected boolean __running = false;

    public synchronized boolean running(){ return __running; }

    public synchronized void running(boolean v){ __running = v; }

    protected boolean add(Executable executable){
        synchronized (__executables){
            return __executables.add(executable);
        }
    }

    protected boolean del(Executable executable){
        synchronized (__executables){
            return __executables.remove(executable);
        }
    }

    @Override
    public void onecycle() {
        novemberizing.rx.Scheduler.Self(this);
        if(!running()){
            running(true);
            __q.lock();
            while(__q.size()>0){
                Executable executable = __q.pop();
                __q.unlock();
                if(executable!=null){
                    if(!add(executable)){
                        Log.d(Tag, this, "add(executable)==false");
                    }
                    executable.execute(this);
                } else {
                    Log.d(Tag, this, "executable==null");
                }
                __q.lock();
            }
            __q.unlock();
            running(false);
        } else {
            Log.d(Tag, this, "__running==false");
        }
    }

    @Override
    public void executed(Executable executable) {
        if(executable!=null){
            if(!del(executable)){
                Log.d(Tag, this, executable, "del(executable)==false");
            }
            __q.lock();
            __q.push(executable);
            __q.resume();
            __q.unlock();
        } else {
            Log.d(Tag, this, null, "executable==null");
        }
    }

    @Override
    public void completed(Executable executable) {
        if(executable!=null){
            if(!del(executable)){
                Log.d(Tag, this, executable, "del(executable)==false");
            }
        } else {
            Log.d(Tag, this, null, "executable==null");
        }
    }

    @Override
    public void execute(Executable executable){
        if (executable != null) {
            if (!add(executable)) {
                Log.d(Tag, this, "add(executable)==false");
            }
            executable.execute(this);
        } else {
            Log.d(Tag, this, "executable==null");
        }
    }

    @Override
    public void clear() {
        int remain;
        running(true);
        do {
            __q.lock();
            while (__q.size() > 0) {
                Executable executable = __q.pop();
                __q.unlock();
                if (executable != null) {
                    if (!add(executable)) {
                        Log.d(Tag, this, "add(executable)==false");
                    }
                    executable.execute(this);
                } else {
                    Log.d(Tag, this, "executable==null");
                }
                __q.lock();

            }
            synchronized (__executables){
                remain = __executables.size();
            }
            __q.unlock();
        } while(remain>0);
        running(false);
    }

    @Override
    public void dispatch(Executable executable) {
        __q.lock();
        __q.push(executable);
        __q.resume();
        __q.unlock();
    }

    @Override
    public void dispatch(Executable executable, Executable... executables) {
        __q.lock();
        __q.push(executable);
        for(Executable item : executables){
            __q.push(item);
        }
        __q.resume(true);
        __q.unlock();
    }

    @Override
    public void dispatch(Executable[] executables) {
        __q.lock();
        for(Executable item : executables){
            __q.push(item);
        }
        __q.resume(true);
        __q.unlock();
    }
}
