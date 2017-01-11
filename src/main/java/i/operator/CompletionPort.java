package i.operator;

import com.google.gson.annotations.Expose;
import i.Local;
import i.Operator;
import i.Scheduler;
import i.Task;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.Iterator;
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
        @Expose public Collection<Task<T>> tasks;
        @Expose public final Task<T> self;
        @Expose public Collection<Object> results;

        @Override
        synchronized public void set(int it, Object in, Object out){
            if(it==IN){
                this.in = (T) in;
            } else if(it==IN+1){
                this.in = (T) in;
                tasks = (Collection<Task<T>>) out;
                if(tasks!=null) {
                    for (Task<T> task : tasks) {
                        if (task.done()) {
                            results.add(task.o());
                        }
                    }
                    if(results.size()==tasks.size()){ this.out = results; }
                }
            } else if(it==IN+2){
                results.add(out);
                if(results.size()==tasks.size()){ this.out = results; }
            } else if(it==IN+3){
                this.out = out;
            }
        }

        synchronized public boolean completed(){ return out!=null; }

        public Local(T in, Task<T> self) {
            super(in);
            this.self = self;
            results = new LinkedList<>();
        }
    }

    protected Operator<T, ?> __op;

    public CompletionPort(i.func.Single<T, ?> f){
        __op = Op(f);
    }

    @Override
    synchronized public Task<Collection<T>> exec(Task<Collection<T>> task) {
        Task<Collection<T>> current = task;
        if(current!=null && current.it()==IN){
            current = __iterate(in(task, task.v.in));
        }
        if(current!=null && current.it()==IN+1){
            current = __iterate(on(task, task.v.in));
            Collection<Task<T>> tasks = Scheduler.Foreach(task, __op, (T[]) task.i().toArray());
            CompletionPort.Local<Collection<T>> v = (CompletionPort.Local<Collection<T>>) task.v;
            v.set(current.it() - 1, task.v.in, tasks);
            if(v.completed()){
                current = __iterate(current);
            }
        }
        if(current!=null && current.it()==IN+2){
            current = __iterate(__on(current, (Local<Collection<T>>) current.v));
        }
        if(current!=null && current.it()==IN+3){
            out(task, task.v.out);
        }
        return task;
    }

    protected Task<Collection<T>> __on(Task<Collection<T>> task, Local<Collection<T>> v){
        return v.completed() ? task : null;
    }

    @Override
    public i.Local<Collection<T>> declare(Task<Collection<T>> task){
        return new Local<>(task.i(), task);
    }

    @Override
    protected Task<Collection<T>> in(Task<Collection<T>> task, Collection<T> o){
        return task.set(o, null);
    }

    @Override
    protected Task<Collection<T>> on(Task<Collection<T>> task, Collection<T> o) {
        return task.set(o, null);
    }
}
