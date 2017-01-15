package novemberizing.rx.operators;

import novemberizing.rx.Observable;
import novemberizing.rx.Operator;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Sync<T, Z> extends Operator<T, Z> {
    private static final String Tag = "Sync";

    public static class Internal<T, Z> extends Operator.Internal<T, Z> {

        private final LinkedList<Local<T, Z>> __tasks = new LinkedList<>();
        private Local<T, Z> __current;

        protected Local<T, Z> exec(T o){
            Local<T, Z> task = new Local<>(o, null, parent);

            synchronized (__tasks) {
                if(__current==null) {
                    __current = task;
                    __observableOn.dispatch(__current);
                } else {
                    __tasks.addLast(task);
                }
            }

            return task;
        }

        protected Observable<Local<T, Z>> emit(Local<T, Z> o){
            Log.f(Tag, this, o);

            __next(o);

            synchronized (__tasks){
                __current = null;
                if(__tasks.size()>0){
                    __current = __tasks.pollFirst();
                    __observableOn.dispatch(__current);
                }
            }

            return this;
        }

        protected Internal(Operator<T, Z> parent) {
            super(parent);
            __current = null;
        }
    }

    protected Operator.Internal<T, Z> initialize(){
        return internal = new Sync.Internal<>(this);
    }

}
