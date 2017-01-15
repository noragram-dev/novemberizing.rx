package novemberizing.rx.operators;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Func;
import novemberizing.ds.Interest;
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

    public abstract static class Internal<T, Z> extends Operator.Internal<T, Z> implements Interest {

        public Internal(Operator<T, Z> p) {
            super(p);
        }
    }

    public static class Observal<T, U, Z> extends Internal<T, Z> {
        private T __first;
        private U __second;

        private novemberizing.ds.func.Pair<T, U, Boolean> __condition;

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

        @Override
        public boolean interest() {
            return __condition.call(__first,__second);
        }
    }

    public static class Functional<T, Z> extends Internal<T, Z> {

        private T __first;
        protected Func<T, Boolean> __condition;

        @Override
        synchronized protected Operator.Local<T, Z> exec(T o){
            Local<T, Object, Z> task;

            if(__condition.call(o)) {
                task = new Local<>(__first = o, null, null, parent);
                __observableOn.dispatch(task);
            } else {
                task = new Local<>(o, null, parent);
            }

            return task;
        }

        @Override public Observable<Operator.Local<T,Z>> complete(){ return super.complete(); }
        @Override public Observable<Operator.Local<T,Z>> error(Throwable e){ return super.error(e); }

        public Functional(Condition<T, Z> p, Func<T, Boolean> condition) {
            super(p);
            __condition = condition;
        }

        @Override
        public boolean interest() {
            return __condition.call(__first);
        }
    }

    protected Internal<T, Z> __interest;
    protected Internal<T, Z> initialize(){ return null; }
    public Condition(Func<T, Boolean> f){
        internal = __interest = new Functional<>(this, f);

    }

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

        internal = observal;
    }

    public boolean interest(){ return __interest.interest(); }
}
