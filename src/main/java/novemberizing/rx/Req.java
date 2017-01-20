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
@SuppressWarnings("WeakerAccess")
public class Req<Z> {
    private Observable<Z> __completionPort;
    private Replayer<Z> __replayer;
    private Z __out;
    private Observable<Z> __observable;
    private final novemberizing.ds.Exec __req;
    private final novemberizing.ds.on.Single<Res<Z>> __ret;

    public Req(novemberizing.ds.on.Single<novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                }
            }
        };
        __req = new On.Exec.Empty<>(on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A> Req(A first, novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                }
            }
        };
        __req = new On.Exec.Single<>(Tuple.New(first), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B> Req(A first, B second, novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                }
            }
        };
        __req = new On.Exec.Pair<>(Tuple.New(first, second), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
    }

    public <A, B, C> Req(A first, B second, C third ,novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Res<Z>>> on){
        __ret = new novemberizing.ds.on.Single<Res<Z>>(){
            @Override
            public void on(Res<Z> o) {
                try {
                    next(o.out());
                } catch(Exception e){
                    error(e);
                }
            }
        };
        __req = new On.Exec.Triple<>(Tuple.New(first, second, third), on, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
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
        __req = new Func.Exec.Single<>(Tuple.New(first), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
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
        __req = new Func.Exec.Pair<>(Tuple.New(first, second), func, __ret);
        __observable = null;
        __completionPort = null;
        __replayer = new Replayer<>(Infinite);
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
            error(e);
            complete();
        }
    }

    private void error(Throwable e){
        __observable.error(e);
        __replayer.error(e);
    }

    private void next(Z o){
        __out = o;
        __observable.emit(o);
        __replayer.add(o);
    }

    private void complete(){
        __replayer.complete(__out);
    }
}
