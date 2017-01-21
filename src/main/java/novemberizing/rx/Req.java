package novemberizing.rx;

import novemberizing.ds.*;
import novemberizing.ds.func.Single;
import novemberizing.rx.operators.Composer;
import novemberizing.rx.operators.Condition;
import novemberizing.rx.operators.Sync;
import novemberizing.util.Log;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
@SuppressWarnings({"WeakerAccess", "unused", "Convert2Lambda"})
public class Req<Z> implements Executable {
    private static final String Tag = "Req";

    private Observable<Z> __completionPort;
    private Replayer<Z> __replayer;
    private Z __out;
    private Observable<Z> __observable;
    private final novemberizing.ds.Exec __req;
    private final novemberizing.ds.on.Single<Res<Z>> __ret;
    private boolean __completed;
    private Executor __executor;
    private boolean __executed;

    public Req(novemberizing.ds.on.Single<novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    if(o.exception()!=null) {
                        error(o.exception());
                    } else {
                        next(o.out());
                    }
                } catch(Exception e){
                    error(e);
                } finally {
                    if(o.completed()){
                        complete();
                    }
                }
            }
        };
        __req = new On.Exec.Empty<>(on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A> Req(A first, novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    if(o.exception()!=null) {
                        error(o.exception());
                    } else {
                        next(o.out());
                    }
                } catch(Exception e){
                    error(e);
                } finally {
                    if(o.completed()){
                        complete();
                    }
                }
            }
        };
        __req = new On.Exec.Single<>(new novemberizing.ds.tuple.Single<>(first), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A, B> Req(A first, B second, novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {

                try {
                    if(o.exception()!=null) {
                        error(o.exception());
                    } else {
                        next(o.out());
                    }
                } catch(Exception e){
                    error(e);
                } finally {
                    if(o.completed()){
                        complete();
                    }
                }
            }
        };
        __req = new On.Exec.Pair<>(new novemberizing.ds.tuple.Pair<>(first, second), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    if(o.exception()!=null) {
                        error(o.exception());
                    } else {
                        next(o.out());
                    }
                } catch(Exception e){
                    error(e);
                } finally {
                    if(o.completed()){
                        complete();
                    }
                }
            }
        };
        __req = new On.Exec.Triple<>(new novemberizing.ds.tuple.Triple<>(first, second, third), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public Req(novemberizing.ds.func.Empty<Res<Z>> func){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                } finally {
                    complete();
                }
            }
        };
        __req = new Func.Exec.Empty<>(func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A> Req(A first, novemberizing.ds.func.Single<A, Res<Z>> func){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                } finally {
                    complete();
                }
            }
        };
        __req = new Func.Exec.Single<>(new novemberizing.ds.tuple.Single<>(first), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A, B> Req(A first, B second, novemberizing.ds.func.Pair<A, B, Res<Z>> func){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                } finally {
                    complete();
                }
            }
        };
        __req = new Func.Exec.Pair<>(new novemberizing.ds.tuple.Pair<>(first, second), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.func.Triple<A, B, C, Res<Z>> func){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                } finally {
                    complete();
                }
            }
        };
        __req = new Func.Exec.Triple<>(new novemberizing.ds.tuple.Triple<>(first, second, third), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
        __completed = false;

        __executor = null;
        __executed = false;
    }

    synchronized protected boolean executed(){ return __executed; }

    @Override
    public void execute(Executor executor) {
        synchronized (this){
            if(__executed){
                Log.d(Tag, "__executed==true");
                return;
            }
            __executed = true;
        }
        if(__executor!=null){
            Log.d(Tag, this, "__executor!=null");
        }
        __executor = executor;
        exec();
    }

    @Override
    public boolean completed() { return __completed; }


    protected void set(Observable<Z> observable){
        __observable = observable;
    }

    protected void exec(){
        try {
            __req.exec();
        } catch(Exception e){
            error(e);
            complete();
        }
    }

    private void error(Throwable e){
        __observable.error(e);
        if(__completionPort!=null){
            __completionPort.error(e);
        } else {
            __replayer.error(e);
        }
    }

    private void next(Z o){
        __out = o;
        __observable.emit(o);
        if(__completionPort!=null) {
            __completionPort.emit(o);
        } else {
            __replayer.add(o);
        }
    }

    protected void complete() {
        if(!__completed) {
            Executor executor = __executor;

            __completed = true;
            __executor = null;

            if (__completionPort != null) {
                __completionPort.complete();
            } else {
                __replayer.complete(__out);
            }

            if (executor != null) {
                executor.completed(this);
            } else {
                Log.d(Tag, "executor is null");
            }
        } else {
            Log.d(Tag, "completed");
        }
    }


    public Observable<Z> subscribe(Observer<Z> observer){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return __completionPort.subscribe(observer);
    }

    public Observable<Z> unsubscribe(Observer<Z> observer){ return __completionPort!=null ? __completionPort.unsubscribe(observer) : null; }
    public Observable<Z> unsubscribe(){ return __completionPort!=null ? __completionPort.unsubscribe() : null; }


    public <U> Sync<Z, U> sync(Single<Z, U> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Sync<Z, U>) __completionPort.subscribe(Operator.Sync(f));
    }
    public <U> Sync<Z, U> sync(novemberizing.ds.on.Pair<Operator.Task<Z, U>, Z> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Sync<Z, U>) __completionPort.subscribe(Operator.Sync(f));
    }

    public <U, V> Composer<Z, U, V> compose(Observable<U> secondary, novemberizing.ds.func.Pair<Z, U, V> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Composer<Z, U, V>) __completionPort.subscribe(Operator.Composer(secondary,f));
    }

    public <U, V> Condition<Z, U, V> condition(Observable<U> observable, novemberizing.ds.func.Pair<Z, U, Boolean> condition , novemberizing.ds.func.Pair<Z, U, V> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return (Condition<Z, U, V>) __completionPort.subscribe(Operator.Condition(observable,condition,f));
    }

    public <U> Operator<Z, U> condition(Single<Z, Boolean> condition, Single<Z, U> f){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        return __completionPort.subscribe(Operator.Condition(condition, f));
    }

    public Observable<Z> replay(int limit){
        if(__completionPort==null){
            __completionPort = new Observable<>(__replayer);
        }
        if(limit==0){
            __completionPort.__replayer = null;
        } else if(__replayer==null){
            __completionPort.__replayer = new Replayer<>(limit);
        } else {
            __completionPort.__replayer.limit(limit);
        }
        return __completionPort;
    }
}
