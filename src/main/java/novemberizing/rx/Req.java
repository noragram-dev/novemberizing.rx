package novemberizing.rx;

import novemberizing.ds.Func;
import novemberizing.ds.On;
import novemberizing.ds.Tuple;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public class Req<T extends Tuple, Z> {
    private Observable<Z> __observable;
    private novemberizing.ds.Exec __req;
    private novemberizing.ds.on.Single<Z> __ret = new novemberizing.ds.on.Single<Z>(){
        @Override
        public void on(Z o) {
            try {
                __observable.emit(o);
            } catch(Exception e){
                __observable.error(e);
            }
        }
    };

    public Req(novemberizing.ds.on.Single<novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Empty<>(on, __ret);
    }

    public <A> Req(A first, novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Single<>(Tuple.New(first), on, __ret);
    }

    public <A, B> Req(A first, B second, novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Pair<>(Tuple.New(first, second), on, __ret);
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Z>> on){
        __req = new On.Exec.Triple<>(Tuple.New(first, second, third), on, __ret);
    }

    public Req(novemberizing.ds.func.Empty<Z> func){
        __req = new Func.Exec.Empty<>(func, __ret);
    }

    public <A> Req(A first, novemberizing.ds.func.Single<A, Z> func){
        __req = new Func.Exec.Single<>(Tuple.New(first), func, __ret);
    }

    public <A, B> Req(A first, B second, novemberizing.ds.func.Pair<A, B, Z> func){
        __req = new Func.Exec.Pair<>(Tuple.New(first, second), func, __ret);
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.func.Triple<A, B, C, Z> func){
        __req = new Func.Exec.Triple<>(Tuple.New(first, second, third), func, __ret);
    }


    public void exec(){
        try {
            __req.exec();
        } catch(Exception e){
            __observable.error(e);
        }
    }
}
