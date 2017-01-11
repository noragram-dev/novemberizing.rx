package i.operator;

import i.Local;
import i.Operator;
import i.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public abstract class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    protected LinkedList<Task<T>> __tasks = new LinkedList<>();
    protected Task<T> __current;

    public Sync(){
        Log.f(Tag, "");
        __current = null;
    }

    synchronized boolean running(){ return __current!=null; }

    @Override
    protected Task<T> in(Task<T> task, T o){
        Log.f(Tag, "");
        if(task!=null) {
            if (!running()) {
                synchronized (this) {
                    __current = task;
                }
                return task;
            } else {
                synchronized (this) {
                    __tasks.addLast(task);
                }
            }
        }
        return null;
    }

    @Override
    protected void out(Task<T> task, Object o){
        Log.f(Tag, "");
        super.out(task, o);
        synchronized (this){
            __current = __tasks.size()>0 ? __tasks.pollFirst() : null;
            if(__current!=null) {
                __current.next();
            }
        }
    }
}
