package novemberizing.rx.operators;

import com.google.gson.annotations.Expose;
import novemberizing.rx.Observable;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Condition<T, Z> extends Operator<T, Z> {

    private static final String Tag = "Condition";

    public static class Local<T, U, Z> extends Operator.Local<T, Z> {
        @Expose public U second;
        public Local(T first, U second, Z out, Operator<T, Z> op) {
            super(first, out, op);
            this.second = second;
        }

        public Local(T first, U second, Operator<T, Z> op){
            super(first, null, op);
            this.second = second;
            __completed = true;
        }
    }

    public static class Observal<T, U, Z> extends Internal<T, Z> {
        T __first;
        U __second;

        protected novemberizing.ds.func.Pair<T, U, Boolean> __condition;

        synchronized protected void second(U o){
            __second = o;
            exec();
        }

        protected Local<T, U, Z> exec(){
            Local<T, U, Z> task;

            if(__condition.call(__first,__second)) {
                task = new Local<>(__first, __second, null, parent);
                __observableOn.dispatch(task);
            } else {
                task = new Local<>(__first, __second, parent);
            }

            return task;
        }

        @Override
        synchronized protected Operator.Local<T, Z> exec(T o){
            __first = o;
            return exec();
        }

        @Override public Observable<Operator.Local<T,Z>> complete(){ return super.complete(); }
        @Override public Observable<Operator.Local<T,Z>> error(Throwable e){ return super.error(e); }

        public Observal(Condition<T, Z> p, novemberizing.ds.func.Pair<T, U, Boolean> condition) {
            super(p);
            __condition = condition;
        }
    }

    protected final Observal<T, ?, Z> __observal;

    protected Internal<T, Z> initialize(){ return null; }

    public <U> Condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition){
        Observal<T, U, Z> observal = new Observal<>(this, condition);
        observable.subscribe(new Subscriber<U>() {
            @Override
            public void onNext(U o) {
                observal.second(o);
            }

            @Override
            public void onComplete() {
                observal.complete();
            }

            @Override
            public void onError(Throwable e) {
                observal.error(e);
            }
        });

        internal = __observal = observal;
    }
}
