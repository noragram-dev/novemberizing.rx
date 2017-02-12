package novemberizing.rx.requests;

import java.util.ArrayList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 2. 12.
 */
public class Multi extends novemberizing.rx.Req<Object> {

    public static Multi Gen(novemberizing.rx.Req.Factory<?> req, novemberizing.rx.Req.Factory<?>... requests){
        Multi multi = new Multi();
        return multi.add(req, requests);
    }

    public static Multi Gen(novemberizing.rx.Req.Factory<?>[] requests){
        Multi multi = new Multi();
        return multi.add(requests);
    }

    private ArrayList<novemberizing.rx.Req.Factory<?>> __requests = new ArrayList<>();
    public Multi add(novemberizing.rx.Req.Factory<?> req, novemberizing.rx.Req.Factory<?>... requests){
        if(req!=null){
            __requests.add(req);
        }
        for(novemberizing.rx.Req.Factory<?> request : requests){
            __requests.add(request);
        }
        return this;
    }

    public Multi add(novemberizing.rx.Req.Factory<?>[] requests){
        for(novemberizing.rx.Req.Factory<?> request : requests){
            __requests.add(request);
        }
        return this;
    }

    public Multi() {
        __req = new novemberizing.ds.On.Exec.Empty<>(new novemberizing.ds.on.Single<novemberizing.rx.Req.Callback<Object>>(){
            @Override
            public void on(Callback<Object> o) {
                novemberizing.rx.Req.Chain((novemberizing.rx.Req.Factory<?>[])__requests.toArray())
                        .success(new novemberizing.ds.on.Empty(){
                            @Override public void on() { __callback.complete(); }
                        })
                        .fail(new novemberizing.ds.on.Single<Throwable>(){
                            @Override public void on(Throwable e) {
                                __callback.error(e);
                                __callback.complete();
                            }
                        });
            }
        });
    }


}
