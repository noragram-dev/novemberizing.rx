package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Executor;
import novemberizing.ds.func.Single;
import novemberizing.rx.operators.Composer;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Switch;
import novemberizing.rx.operators.Sync;
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

    public static class Task<T, Z> {
        private Operator.Next<?, Z> __task;
        @Expose private final T in;
        private Operator<T, Z> __operator;

        public T in(){ return in; }

        public final void next(Z o){
            __task.next(o);
        }

        public final void error(Throwable e){
            __task.error(e);
        }

        public final void complete(){
            __task.complete(this);
            if (__operator.__observer != null) {
                __operator.__observer.onNext(this);
            }
        }

        protected <U> Task(Operator.Next<U, Z> task, T in, Operator<T, Z> operator){
            __task = task;
            this.in = in;
            __operator = operator;
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
            __operator.in(this, new Operator.Task<>(this, in, __operator));
        }

        @Override
        protected void complete(Operator.Task<?, Z> task){
            complete();
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
                __operator.in(this, new Operator.Task<>(this, o, __operator));
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
    protected Observer<Operator.Task<T, U>> __observer = null;

    @Override
    public Scheduler observeOn() { return __observeOn; }

    public Observer<T> observeOn(Scheduler scheduler){
        __observeOn = scheduler;
        return this;
    }

    protected Operator.Next<?, U> re(Operator.Task<T, U> task){
        task.__task.__executed = false;
        __observableOn.dispatch(task.__task);
        return task.__task;
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

    @SafeVarargs
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

    public final novemberizing.rx.Task<Collection<T>, U> bulk(Collection<T> list){
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
            Log.d(Tag, "check this logic");
            __replayer.add(snapshot(__current));
        }
        return snapshot(__current);
    }

    protected Throwable exception(Throwable e){
        Log.d(Tag, "check this logic");
        if(__replayer!=null){
            Log.d(Tag, "check this logic");
            __replayer.error(e);
        }
        return e;
    }

    protected U done(){
        Log.d(Tag, "check this logic");
        if(__replayer!=null){
            Log.d(Tag, "check this logic");
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

    public static <T, Z> Operator<T, Z> Op(Single<T, Z> f){
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

    public static <T, Z> Operator<T, Z> Op(novemberizing.ds.on.Pair<Operator.Task<T, Z>,T> f){
        return new Operator<T, Z>() {
            @Override
            protected void on(Task<T, Z> task, T in) {
                try {
                    f.on(task, in);
                } catch (Exception e) {
                    task.error(e);
                }
                task.complete();
            }
        };
    }

    public static <T, Z> Sync<T, Z> Sync(Single<T, Z> f){
        return new Sync<T, Z>() {
            @Override
            protected void on(Task<T, Z> task, T in) {
                try {
                    task.next(f.call(in));
                    task.complete();
                } catch (Exception e){
                    task.error(e);
                }
            }
        };
    }

    public static <T, Z> Sync<T, Z> Sync(novemberizing.ds.on.Pair<Operator.Task<T, Z>,T> f){
        return new Sync<T, Z>() {
            @Override
            protected void on(Task<T, Z> task, T in) {
                try {
                    f.on(task, in);
                } catch (Exception e) {
                    task.error(e);
                }
                task.complete();
            }
        };
    }

    public static <T, U, Z> Composer<T, U, Z> Composer(Observable<U> secondary, novemberizing.ds.func.Pair<T, U, Z> f){
        return new Composer<>(secondary, f);
    }

    public static <T, U, Z> Condition<T, U, Z> Condition(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition , novemberizing.ds.func.Pair<T, U, Z> f){
        return new Condition<>(observable,condition,f);
    }

    public static <T, Z> Operator<T, Z> Condition(Single<T, Boolean> condition, Single<T, Z> f){
        return new Operator<T, Z>() {
            @Override
            protected void on(Task<T, Z> task, T in) {
                try {
                    if (condition.call(in)) {
                        task.next(f.call(in));
                    }
                } catch(Exception e){
                    task.error(e);
                }
                task.complete();
            }
        };
    }

//    public static <T, Z> Switch<T, Z> Switch(Single<T, Integer> hash){
//        return new Switch<>(hash);
//    }

//    public static <T, Z> Block<T, Z> Block()

    protected static <T, Z> novemberizing.rx.Task<Collection<Z>, Z> Bulk(Operator<T, Z> operator, Collection<Z> list){
        return Observable.Bulk(operator, list);
    }

//    protected static <Z> novemberizing.rx.Task<Collection<T>, Z>

}
