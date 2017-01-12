package novemberizing.rx;

import novemberizing.rx.operator.Block;
import novemberizing.util.Log;

import java.util.HashSet;

import static novemberizing.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 12.
 */
public class Wrap<T, U> extends Observable<U> implements Observer<T> {
    private static final String Tag = "Wrap";

    private Observable<U> __observable;

    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn;
    private final Block<T, U> __completionPort;
    private final HashSet<Task<T>> __tasks = new HashSet<>();

    public Wrap(novemberizing.rx.func.Single<T, ?> f, Observable<U> observable){
        __observable = observable;
        __completionPort = new Block<T, U>(f){

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
                synchronized (__tasks) {
                    if(!__tasks.remove(task)){
                        Log.e(Tag, new Throwable(), this, task, o);
                    }
                }
                super.out(task, o);
                next((U) o);
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


    protected U snapshot(U o){ return __observable.snapshot(o); }

    public void update(U o){ __observable.update(o); }

    public Observable<U> replay(int limit) {
        __observable.replay(limit);
        return this;
    }

    protected void next(U o){ __observable.next(o); }

    public void error(Throwable e){ __observable.error(e); }

    public void complete(){ __observable.complete(); }

    public <V> Subject<U, V> subscribe(Subject<U, V> subject){ return __observable.subscribe(subject); }

    public Observable<U> subscribe(Observer<U> observer){ return __observable.subscribe(observer); }

    public Observable<U> unsubscribe(Observer<U> observer){ return __observable.unsubscribe(observer); }

    public Scheduler publishOn(){ return __publishOn; }

    synchronized public Observable<U> publishOn(Scheduler scheduler){ return __observable.publishOn(scheduler); }

    public <V> Subject<U, V> op(novemberizing.rx.func.Single<U, ?> f){ return __observable.op(f); }

    public <V> Observable<V> wrap(novemberizing.rx.func.Single<U, V> f, Observable<V> observable){ return __observable.wrap(f, observable); }
}
