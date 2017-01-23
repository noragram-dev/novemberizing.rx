package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Executor;
import novemberizing.ds.func.Empty;
import novemberizing.ds.func.Pair;
import novemberizing.ds.func.Single;
import novemberizing.rx.operators.*;
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
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Operator<T, U> extends Observable<U> implements Observer<T> {
    private static final String Tag = "Operator";

    protected static abstract class Next<T, Z> extends novemberizing.rx.Task<T, Z> {
        protected void complete(Operator.Task<?, Z> task){
            complete();
        }



        private Next(T in) {
            super(in);
        }
    }

    public static class Task<T, Z> extends Local {
        private Operator.Next<?, Z> __task;
        @Expose private final T in;
        private Operator<T, Z> __operator;

        public T in(){ return in; }

        public final void next(Z o){
            __task.out = o;
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
        private Operator<T, Z> __operator;

        private Exec(T in, Operator<T, Z> operator) {
            super(in);
            __operator = operator;
        }

        @Override
        protected void execute() {
            __operator.in(this, new Operator.Task<>(this, in, __operator));
        }

        @Override
        public void complete(Operator.Task<?, Z> task){
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

        private Operator<T, Z> __operator;
        private LinkedList<Operator.Task<?, Z>> __completions = new LinkedList<>();

        private Execs(Collection<T> in, Operator<T, Z> operator) {
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

    @Expose public final Counter tasks = new Counter();
    private final HashSet<Observable<T>> __observables = new HashSet<>();
    private Scheduler __observeOn = Scheduler.New();
    protected Observer<Operator.Task<T, U>> __observer = null;


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
        tasks.increase();
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
        tasks.increase(list.size());
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
        tasks.increase(list.size());
        if(__observableOn==Scheduler.Self()){
            __observableOn.execute(task);
        } else {
            __observableOn.dispatch(task);
        }

        return task;
    }

    public final novemberizing.rx.Task<Collection<T>, U> bulk(Collection<T> list){
        Execs<T, U> task = new Execs<>(list, this);
        tasks.increase(list.size());
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


    public static <Z> Req<Z> Req(novemberizing.ds.func.Empty<Z> func){
        return new Req<>(func);
    }

    public static <A, Z> Req<Z> Req(A first, novemberizing.ds.func.Single<A, Z> func){
        return new Req<>(first, func);
    }

    public static <A, B, Z> Req<Z> Req(A first, B second, novemberizing.ds.func.Pair<A, B, Z> func){
        return new Req<>(first, second, func);
    }

    public static <A, B, C, Z> Req<Z> Req(A first, B second, C third ,novemberizing.ds.func.Triple<A, B, C, Z> func){
        return new Req<>(first, second, third, func);
    }


    public static <Z> Req<Z> Req(novemberizing.ds.on.Single<Req.Callback<Z>> on){
        return new Req<>(on);
    }

    public static <A, Z> Req<Z> Req(A first, novemberizing.ds.on.Pair<A, Req.Callback<Z>> on){
        return new Req<>(first, on);
    }

    public static <A, B, Z> Req<Z> Req(A first, B second, novemberizing.ds.on.Triple<A, B, Req.Callback<Z>> on){
        return new Req<>(first, second, on);
    }

    public static <A, B, C, Z> Req<Z> Req(A first, B second, C third ,novemberizing.ds.on.Quadruple<A, B, C, Req.Callback<Z>> on){
        return new Req<>(first, second, third, on);
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

//    public static <T, Z> Operator<T, Z> Op(Operator<T, Z> op){
//        return new Operator<T, Z>() {
//            @Override
//            protected void on(Task<T, Z> task, T in) {
//                op.exec(in).subscribe(new Subscriber<Z>() {
//                    @Override
//                    public void onNext(Z o) {
//                        task.next(o);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        task.error(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        task.complete();
//                    }
//                });
//            }
//        };
//    }

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

    public static <T, U, Z> Completion<T, U, Z> Completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Boolean> condition , novemberizing.ds.func.Pair<T, U, Z> f){
        return new Completion<>(observable,condition,f);
    }

    public static <T, U, Z> Completion<T, U, Z> Completion(Observable<U> observable, novemberizing.ds.func.Pair<T, U, Z> f){
        return new Completion<>(observable, null,f);
    }

    public static <T, Z> Switch<T, Z> Switch(Single<T, Integer> hash){
        return new Switch<>(hash);
    }

    public static <T, Z> Block.Op<T, Z> Block(Single<T, Z> f){
        return Block.<T, Z, Z>begin(f).ret(new Single<Z, Z>() {
            @Override
            public Z call(Z o) {
                return o;
            }
        });
    }

    public static <T, Z> Block.Op<T, Z> Block(Operator<T, Z> op){
        return Block.<T, T, Z>begin(new Single<T, T>(){
            @Override
            public T call(T o) {
                return o;
            }
        }).next(op).ret(new Single<Z, Z>() {
            @Override
            public Z call(Z o) {
                return o;
            }
        });
    }


    public static <T, Z> If<T, Z> If(Single<T, Boolean> condition, Block.Op<T, Z> block){
        return new If<>(condition, block);
    }

    public static <T, Z> If<T, Z> If(Single<T, Boolean> condition, novemberizing.ds.func.Single<T, Z> f){
        return new If<>(condition, f);
    }

    public static <T, Z> If<T, Z> If(Single<T, Boolean> condition, Operator<T, Z> op){
        return new If<>(condition, op);
    }

    protected static <T, Z> novemberizing.rx.Task<Collection<Z>, Z> Bulk(Operator<T, Z> operator, Collection<Z> list){
        return Observable.Bulk(operator, list);
    }

    protected static <T, Z> void Complete(Operator.Exec<T, Z> exec){
        exec.complete();
    }

    protected static <T, Z> void Complete(Operator.Execs<T, Z> exec){
        exec.complete();
    }

    public static <T> Until<T> Until(Block.Op<T, T> block, novemberizing.ds.func.Single<T, Boolean> condition){
        return new Until<>(block,condition);
    }

    public static <T> Until<T> Until(novemberizing.ds.func.Single<T, T> f, novemberizing.ds.func.Single<T, Boolean> condition){
        return new Until<>(f,condition);
    }

    public static <T> Until<T> Until(Operator<T, T> op, novemberizing.ds.func.Single<T, Boolean> condition){
        return new Until<>(op,condition);
    }

    public static <T> While<T> While(novemberizing.ds.func.Single<T, Boolean> condition, Block.Op<T, T> block){
        return new While<>(condition, block);
    }

    public static <T> While<T> While(novemberizing.ds.func.Single<T, Boolean> condition, novemberizing.ds.func.Single<T, T> f){
        return new While<>(condition, f);
    }

    public static <T> While<T> While(novemberizing.ds.func.Single<T, Boolean> condition, Operator<T, T> f){
        return new While<>(condition, f);
    }

    public static <T, U> For<T, U> For(Empty<U> initializer, Pair<T, U, Boolean> condition, Single<U, U> internal, novemberizing.ds.func.Single<T, T> external){
        return new For<>(initializer, condition, internal, external);
    }

    public static <T, U> For<T, U> For(Empty<U> initializer, Pair<T, U, Boolean> condition, Single<U, U> internal, Operator<T, T> external){
        return new For<>(initializer, condition, internal, external);
    }

    public static <T, U> For<T, U> For(Empty<U> initializer, Pair<T, U, Boolean> condition, Single<U, U> internal, Block.Op<T, T> external){
        return new For<>(initializer, condition, internal, external);
    }
}
