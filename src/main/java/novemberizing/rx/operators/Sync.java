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
public abstract class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    public static class Internal<T, U> extends Operator.Internal<T, U> {

        private final LinkedList<Local<T, U>> __tasks = new LinkedList<>();
        private Local<T, U> __current;

        protected Local<T, U> exec(T o){
            Local<T, U> task = new Local<>(o, parent);

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

        protected Observable<Local<T, U>> emit(Local<T, U> o){
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

        protected Internal(Operator<T, U> parent) {
            super(parent);
            __current = null;
        }
    }

    protected Operator.Internal<T, U> initialize(){
        return internal = new Sync.Internal<>(this);
    }

}
