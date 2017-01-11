package i.operator;

import i.Operator;
import i.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

import static i.Iteration.IN;


/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public abstract class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    public static class Local<T> extends i.Local<T> {

        public Object get(int it){
            if(it==IN){
                return in;
            } else if(it== IN +1){
                return in;
            } else if(it== IN +2 || it==IN+3){
                return out;
            }
            return null;
        }

        public <U> U get(int it, Class<U> c) throws ClassCastException {
            if(it==IN){
                return c.cast(in);
            } else if(it== IN +1){
                return c.cast(in);
            } else if(it== IN +2 || it==IN+3){
                return c.cast(out);
            }
            return null;
        }

        public Local(T in){
            super(in);
            Log.f(Tag, "");
        }
    }

    protected LinkedList<Task<T>> __tasks = new LinkedList<>();
    protected Task<T> __current;

    public Sync(){
        Log.f(Tag, "");
        __current = null;
    }

    synchronized boolean running(){ return __current!=null; }

    private Task<T> precede(Task<T> task){
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

    public Task<T> exec(Task<T> task) {
        Task<T> current = task;
        if(current!=null && current.it()== IN){
            current = precede(task);
            if(current!=null){ current.it(current.it()+1); }
        }
        if(current!=null && current.it()== IN +1){
            current = in(task, task.v.in);
            if(current!=null){ current.it(current.it()+1); }
        }
        if(current!=null && current.it()== IN +2){
            out(task, task.v.get(current.it()));
        }
        return task;
    }

    @Override
    protected void out(Task<T> task, Object o){
        super.out(task, o);
        synchronized (this){
            __current = __tasks.size()>0 ? __tasks.pollFirst() : null;
            if(__current!=null) {
                __current.next();
            }
        }
    }

    @Override
    public i.Local<T> declare(Task<T> task){
        return new Local<>(task.i());
    }

    @Override
    public int done(){ return IN + 3; }
}
