package i;

import i.operator.Block;

import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Subject<T, U> extends Observable<U> implements Observer<T> {
    private static final String Tag = "";

    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn;
    private final Block<T, U> __completionPort;
    private final HashSet<Task<T>> __tasks = new HashSet<>();

    public Subject(i.func.Single<T, ?> f){
        __completionPort = new Block<T, U>(f){
            public Operator<?, U> back(){ return (Operator<?, U>) this.last(); }

            @Override
            protected Task<T> in(Task<T> task, T o){
                synchronized (__tasks) {
                    if(!__tasks.add(task)){
                        Log.e(Tag, new Throwable(), this, task, o);
                    }
                }
                return task.set(o, null);
            }

            @Override
            protected void out(Task<T> task, Object o){
                task.out(o);
                synchronized (__tasks) {
                    if(!__tasks.remove(task)){
                        Log.e(Tag, new Throwable(), this, task, o);
                    }
                }
                next((U) o);
                if (__next != null) {
                    __next(task);
                } else {
                    __up(task);
                }
            }
        };
    }

    @Override
    synchronized public void onNext(T o) {
        Log.i(Tag, o);
        Scheduler.Exec(__observeOn==null ? Scheduler.Local() : __observeOn, __completionPort, o);
    }

    @Override
    synchronized public void onError(Throwable e) {
        Log.i(Tag, e);
    }

    @Override
    synchronized public void onComplete() {
        Log.i(Tag, "onComplete");
    }

    @Override
    synchronized public void onSubscribe(Observable<T> observable) {
        synchronized (__observables) {
            __observables.add(observable);
        }
    }

    @Override
    synchronized public void onUnsubscribe(Observable<T> observable) {
        synchronized (__observables) {
            __observables.add(observable);
        }
    }

    @Override
    public Scheduler observeOn() { return __observeOn; }
}
