package novemberizing.rx;

import novemberizing.ds.Executor;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Operator<T, U> extends Observable<U> implements Observer<T> {
    private static final String Tag = "Operator";

    public static abstract class Next<T, Z> extends novemberizing.rx.Task<T, Z> {
        protected void complete(Operator.Task<?, Z> task){
            complete();
        }

        public Next(T in) {
            super(in);
        }
    }

    protected static class Task<T, Z> {
        private Operator.Next<?, Z> __task;
        private final T in;

        public T in(){ return in; }

        public final void next(Z o){
            __task.next(o);
        }

        public final void error(Throwable e){
            __task.error(e);
        }

        public void complete(){
            __task.complete(this);
        }

        protected <U> Task(Operator.Next<U, Z> task, T in){
            __task = task;
            this.in = in;
        }
    }

    public static class Exec<T, Z> extends Operator.Next<T, Z> {
        protected Operator<T, Z> __operator;

        public Exec(T in, Operator<T, Z> operator) {
            super(in);
            __operator = operator;
        }

        @Override
        protected void execute() {
            __operator.in(this, new Operator.Task<>(this, in));
        }

        @Override
        public void next(Z o) {
            __operator.emit(o);
            super.next(o);
        }

        @Override
        public void error(Throwable e) {
            __operator.error(e);
            super.error(e);
        }
    }

    public static class Execs<T, Z> extends Operator.Next<Collection<T>, Z> {

        protected Operator<T, Z> __operator;
        protected LinkedList<Operator.Task<?, Z>> __completions = new LinkedList<>();

        public Execs(Collection<T> in, Operator<T, Z> operator) {
            super(in);
            __operator = operator;
        }

        @Override
        protected void execute() {
            for(T o : in){
                __operator.in(this, new Operator.Task<>(this, o));
            }
        }

        @Override
        protected void complete(Operator.Task<?, Z> task){
            __completions.add(task);
            complete();
        }

        @Override
        protected void complete() {
            if(__completions.size()==in.size()) {
                Executor executor = __executor;

                __completed = true;
                __executor = null;

                __replayer.complete(out);

                if (__completionPort != null) {
                    __completionPort.complete();
                }

                if (executor != null) {
                    executor.completed(this);
                } else {
                    Log.d(Tag, "executor is null");
                }
            } else {
                Log.d(Tag, __completions.size());
            }
        }

        @Override
        public void next(Z o) {
            __operator.emit(o);
            super.next(o);
        }

        @Override
        public void error(Throwable e) {
            __operator.error(e);
            super.error(e);
        }
    }

    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn = Scheduler.New();

    @Override
    public Scheduler observeOn() { return __observeOn; }

    public Observer<T> observeOn(Scheduler scheduler){
        __observeOn = scheduler;
        return this;
    }

    protected void in(Operator.Next<?, U> next, Operator.Task<T, U> task){
        on(task, task.in());
    }

    protected abstract void on(Operator.Task<T, U> task, T in);

    public final novemberizing.rx.Task<T, U> exec(T o){
        Exec<T, U> task = new Exec<>(o, this);
        if(__observableOn==Scheduler.Self()){
            __observableOn.execute(task);
        } else {
            __observableOn.dispatch(task);
        }
        return task;
    }

    public final novemberizing.rx.Task<Collection<T>, U> foreach(T o, T... items){
        LinkedList<T> list = new LinkedList<>();
        list.add(o);
        Collections.addAll(list, items);

        Execs<T, U> task = new Execs<>(list, this);
        if(__observableOn==Scheduler.Self()){
            __observableOn.execute(task);
        } else {
            __observableOn.dispatch(task);
        }
        return task;
    }

    public final novemberizing.rx.Task<Collection<T>, U> foreach(T[] items){
        LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, items);

        Execs<T, U> task = new Execs<>(list, this);
        if(__observableOn==Scheduler.Self()){
            __observableOn.execute(task);
        } else {
            __observableOn.dispatch(task);
        }
        return task;
    }

    @Override
    protected U set(U v){
        __current = snapshot(v);
        if(__replayer!=null) {
            Log.e(Tag, "check this logic");
            __replayer.add(snapshot(__current));
        }
        return snapshot(__current);
    }

    protected Throwable exception(Throwable e){
        Log.e(Tag, "check this logic");
        if(__replayer!=null){
            Log.e(Tag, "check this logic");
            __replayer.error(e);
        }
        return e;
    }

    protected U done(){
        Log.e(Tag, "check this logic");
        if(__replayer!=null){
            Log.e(Tag, "check this logic");
            __replayer.complete(__current);
        }
        return __current;
    }

    @Override
    public void onNext(T o) {
        exec(o);
    }

    @Override
    public void onError(Throwable e) {
        error(e);
    }

    @Override
    public void onComplete() {
        complete();
    }

    protected void onSubscribe(Observable<T> observable) {
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

    protected void onUnsubscribe(Observable<T> observable) {
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

    public static <T, Z> Operator<T, Z> Op(Func<T, Z> f){
        return new Operator<T, Z>() {
            @Override
            public void on(Task<T, Z> task, T in) {
                try {
                    task.next(f.call(in));
                    task.complete();
                } catch (Exception e){
                    task.error(e);
                }
            }
        };
    }

//    public static <T, Z> Operator<T, Z> Op(Runnable r){
//        return new Operator<T, Z>() {
//            @Override
//            public void on(Task<T, Z> task, T in) {
//                try {
//                    task.next(f.call(in));
//                    task.complete();
//                } catch (Exception e){
//                    task.error(e);
//                }
//            }
//        };
//    }
}
