package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.ds.On;
import novemberizing.ds.Tuple;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public class Req<Z> {
    protected Observable<Z> __completionPort;
    protected Replayer<Z> __replayer;
    private Z __out;
    private Observable<Z> __observable;
    private final novemberizing.ds.Exec __req;
    private final novemberizing.ds.on.Single<Z> __ret = new novemberizing.ds.on.Single<Z>(){
        @Override
        public void on(Z o) {
            try {
                __out = o;
                __observable.emit(o);
                __replayer.add(o);
            } catch(Exception e){
                __observable.error(e);
                __replayer.error(e);
            }
        }
    };

    public Req(novemberizing.ds.on.Single<novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Empty<>(on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A> Req(A first, novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Single<>(Tuple.New(first), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B> Req(A first, B second, novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Pair<>(Tuple.New(first, second), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Triple<>(Tuple.New(first, second, third), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public Req(novemberizing.ds.func.Empty<Z> func){
        __req = new Func.Exec.Empty<>(func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A> Req(A first, novemberizing.ds.func.Single<A, Z> func){
        __req = new Func.Exec.Single<>(Tuple.New(first), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B> Req(A first, B second, novemberizing.ds.func.Pair<A, B, Z> func){
        __req = new Func.Exec.Pair<>(Tuple.New(first, second), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.func.Triple<A, B, C, Z> func){
        __req = new Func.Exec.Triple<>(Tuple.New(first, second, third), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    protected void set(Observable<Z> observable){
        __observable = observable;
    }

    protected void exec(){
        try {
            __req.exec();
        } catch(Exception e){
            __observable.error(e);
            __replayer.error(e);
            __replayer.complete(__out);
        }
    }
}
