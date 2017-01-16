package novemberizing.ds;

import novemberizing.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public abstract class Command implements Executable {
    private static final String Tag = "Command";

    protected Executor __executor = null;
    protected boolean __completed = false;
    protected final HashSet<CompletionPort> __completionPorts = new HashSet<>();

    public abstract void execute();

    @Override
    public void execute(Executor executor) {
        Log.f(Tag, this, executor);

        synchronized (this){
            if(__executor!=null){
                Log.e(Tag, new RuntimeException("__executor!=null"));
            }
            __executor = executor;
        }
        execute();
    }

    protected void executed() {
        Log.f(Tag, this);

        Executor executor;
        synchronized (this) {
            executor = __executor;
            __executor = null;
            if (executor == null) {
                Log.e(Tag, new RuntimeException("__executor==null"));
            }
        }
        if(executor!=null) {
            executor.executed(this);
        }
    }

    protected void complete() {
        Log.f(Tag, this);

        Executor executor;
        synchronized (this) {
            __completed = true;
            executor = __executor;
            __executor = null;
            if (executor == null) {
                Log.e(Tag, new RuntimeException("__executor==null"));
            }
            if(__completionPorts!=null){
                Iterator<CompletionPort> it = __completionPorts.iterator();
                while(it.hasNext()){
                    CompletionPort port = it.next();
                    if(port!=null) {
                        port.dispatch(this);
                    }
                    it.remove();
                }
            }
        }

        if(executor!=null){
            executor.completed(this);
        }
    }

    @Override
    synchronized public boolean completed(){ return __completed; }

    @Override
    synchronized public void add(CompletionPort completionPort){
        Log.f(Tag, this, completionPort);
        if(completionPort!=null) {
            if (__completed) {
                completionPort.dispatch(this);
            } else {
                __completionPorts.add(completionPort);
            }
        }
    }
}