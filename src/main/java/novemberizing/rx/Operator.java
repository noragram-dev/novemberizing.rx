package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.util.Log;

import java.util.HashSet;


/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Operator<T, U> extends Observable<U> implements Observer<T>, Func<T, U> {
    private static final String Tag = "Operator";

    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn = Scheduler.New();

    @Override
    public void onSubscribe(Observable<T> observer) {
        if(observer!=null){
            synchronized (__observables){
                if(!__observables.add(observer)){
                    Log.d(Tag, "!__observables.add(observer)");
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
    }

    @Override
    public void onUnsubscribe(Observable<T> observer) {
        if(observer!=null){
            synchronized (__observables){
                if(!__observables.remove(observer)){
                    Log.d(Tag, "!__observables.remove(observer)");
                }
            }
        } else {
            Log.e(Tag, new RuntimeException("observer==null"));
        }
    }

    @Override
    public Scheduler observeOn(){ return __observeOn; }
}
