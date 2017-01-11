package i.operator;

import i.Operator;
import i.Scheduler;
import i.Task;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

import static i.Iteration.IN;


/**
 *
 * Completion(Op(...))
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class CompletionPort<T, U> extends Operator<Collection<T>, Collection<U>> {
    private static final String Tag = "CompletionPort";

    public static class Local<T extends Collection> extends i.Local<T> {
        public Collection<Task<T>> tasks;
        public LinkedList<Object> results;
        private Task<T> __parent;

        @Override
        public Object get(int it){
            if(it== IN) {
                return in;
            } else if(it== IN +1){
                return this;
            } else if(it== IN +2 || it== IN +3){
                return out;
            }
            return null;
        }

        @Override
        public <U> U get(int it, Class<U> c) throws ClassCastException {
            if(it== IN){
                return c.cast(in);
            } else if(it== IN +1){
                return c.cast(this);
            } else if(it== IN +2 || it== IN +3){
                return c.cast(out);
            }
            return null;
        }

        @Override
        synchronized public void set(int it, Object in, Object out){
            Log.f("", this, it, in, out);
            if(it== IN){
                this.in = (T) in;
                this.tasks = (Collection<Task<T>>) out;
            } else if(it== IN +1){
                results.addLast(out);
                if (results.size() == tasks.size()) {
                    this.out = results;
                    __parent.next(null , this.out);
                }
            } else if(it== IN +2){
                this.out = out;
            }
        }

        synchronized public boolean completed(){ return out!=null; }

        public Local(T in, Task<T> parent) {
            super(in);
            results = new LinkedList<>();
            __parent = parent;
        }
    }

    protected Operator<T, ?> __op;

    public CompletionPort(i.func.Single<T, ?> f){
        __op = Op(f);
    }

    @Override
    public Task<Collection<T>> exec(Task<Collection<T>> task) {
        Task<Collection<T>> current = task;
        if(current!=null && current.it()== IN){
            current = in(task, task.v.in);
            if(current!=null){ current.it(current.it()+1); }
        }
        if(current!=null && current.it()== IN +1){
            on(task, (CompletionPort.Local<Collection<T>>) task.v);
        }
        if(current!=null && current.it()== IN +2){
            out(task, task.v.get(current.it()));
        }
        return task;
    }

    @Override
    protected Task<Collection<T>> in(Task<Collection<T>> task, Collection<T> o){
        return task.set(o, Scheduler.Foreach(task, __op, (T[]) o.toArray()));
    }

    protected Task<Collection<T>> on(Task<Collection<T>> task, CompletionPort.Local<Collection<T>> o) {
        return o.completed() ? task : null;
    }

    @Override
    public i.Local<Collection<T>> declare(Task<Collection<T>> task){
        return new CompletionPort.Local<>(task.i(), task);
    }

    public int done(){ return 3; }
}
