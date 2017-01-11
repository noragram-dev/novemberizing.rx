package i.operator;

import i.Operator;
import i.Scheduler;
import i.Task;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

import static i.Iteration.ON;

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
        public final Collection<Task<T>> tasks;
        public final Task<T> parent;
        public Collection<Object> results;

        @Override
        synchronized public void out(Object o){
            results.add(o);
            if(in.size()==results.size()){
                out = results;
                next++;
                parent.executed();
            }
        }

        synchronized public boolean completed(){ return out!=null; }

        public Local(T in, Task<T> parent, Collection tasks) {
            super(in);
            this.parent = parent;
            results = new LinkedList<>();
            this.tasks = (Collection<Task<T>>) tasks;
            synchronized (this){
                for(Task<T> task : this.tasks){
                    if(task.done()){
                        results.add(task.o());
                    }
                }
                if(in.size()==results.size()){ out = results; }
            }
        }
    }

    protected Operator<T, ?> __op;

    public CompletionPort(i.func.Single<T, ?> f){
        __op = Op(f);
    }

    @Override
    protected Task<Collection<T>> in(Task<Collection<T>> task){
        task.v = new CompletionPort.Local<>(task.i(), task, Scheduler.Foreach(task, __op, (T[]) task.i().toArray()));
        CompletionPort.Local<Collection<T>> v = (CompletionPort.Local<Collection<T>>) task.v;
        return task;
    }

    @Override
    protected Task<Collection<T>> on(Task<Collection<T>> task) {
        CompletionPort.Local<Collection<T>> v = (CompletionPort.Local<Collection<T>>) task.v;
        return v.completed() ? task : null;
    }
}
