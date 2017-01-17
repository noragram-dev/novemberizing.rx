package novemberizing.rx;

import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Operator<T, U> extends Observable<U> implements Observer<T> {
    private static final String Tag = "Operator";



    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn = Scheduler.New();
    private Func<T, U> __func;


    @Override
    public Scheduler observeOn() { return __observeOn; }

    @Override
    public Observer<T> observeOn(Scheduler scheduler){
        __observeOn = scheduler;
        return this;
    }

    public void on(Task<T, U> task){
        // task.
        // task.out = // f.call(task.in);

    }

    @Override
    public void onNext(T o) {
        if(__observableOn==Scheduler.Self()){

        } else {

        }
    }

    @Override
    public void onError(Throwable e) {
        error(e);
    }

    @Override
    public void onComplete() {
        complete();
    }

    @Override
    public void onSubscribe(Observable<T> observable) {
        if(observable!=null) {
            synchronized (__observables){
                if(!__observables.add(observable)){
                    Log.d(Tag, this, observable, "!__observables.add(observable)");
                }
            }
        } else {
            Log.d(Tag, this, null, "observable==null");
        }
    }

    @Override
    public void onUnsubscribe(Observable<T> observable) {
        if(observable!=null) {
            synchronized (__observables){
                if(!__observables.remove(observable)){
                    Log.d(Tag, this, observable, "!__observables.remove(observable)");
                }
            }
        } else {
            Log.d(Tag, this, null, "observable==null");
        }
    }
}
