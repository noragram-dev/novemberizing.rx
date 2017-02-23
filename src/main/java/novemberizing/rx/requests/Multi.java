package novemberizing.rx.requests;

import novemberizing.rx.Req;
import novemberizing.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 2. 12.
 */
public class Multi extends novemberizing.rx.Req<Object> {
    private static final String Tag = "Multi";

    public static Multi Gen(novemberizing.rx.Req.Factory<?> req, novemberizing.rx.Req.Factory<?>... requests){
        Multi multi = new Multi();
        return multi.add(req, requests);
    }

    public static Multi Gen(novemberizing.rx.Req.Factory<?>[] requests){
        Multi multi = new Multi();
        return multi.add(requests);
    }

    public static <T> Multi Gen(Collection<novemberizing.rx.Req.Factory<T>> requests){
        Multi multi = new Multi();
        return multi.add(requests);
    }

    public static novemberizing.rx.Req.Factory<Object> Req(novemberizing.rx.Req.Factory<?> req, novemberizing.rx.Req.Factory<?>... requests) {
        return new novemberizing.rx.Req.Factory<Object>(){
            @Override
            public Req<Object> call() {
                novemberizing.rx.Req<Object> executable = Gen(req,requests);
                executable.execute(null);
                return executable;
            }
        };
    }

    public static novemberizing.rx.Req.Factory<Object> Req(novemberizing.rx.Req.Factory<?>[] requests) {
        return new novemberizing.rx.Req.Factory<Object>(){
            @Override
            public Req<Object> call() {
                novemberizing.rx.Req<Object> executable = Gen(requests);
                executable.execute(null);
                return executable;
            }
        };
    }

    public static <T> novemberizing.rx.Req.Factory<Object> Req(Collection<novemberizing.rx.Req.Factory<T>> requests) {
        return new novemberizing.rx.Req.Factory<Object>(){
            @Override
            public Req<Object> call() {
                novemberizing.rx.Req<Object> executable = Gen(requests);
                executable.execute(null);
                return executable;
            }
        };
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

    public <T> Multi add(Collection<novemberizing.rx.Req.Factory<T>> requests){
        for(novemberizing.rx.Req.Factory<?> request : requests){
            __requests.add(request);
        }
        return this;
    }

    public Multi() {
        __req = new novemberizing.ds.On.Exec.Empty<>(new novemberizing.ds.on.Single<novemberizing.rx.Req.Callback<Object>>(){
            @Override
            public void on(Callback<Object> o) {
                novemberizing.rx.Req.Chain(__requests)
                        .success(new novemberizing.ds.on.Empty(){
                            @Override public void on() {
                                Log.d(Tag, "success");
                                __callback.complete();
                            }
                        })
                        .fail(new novemberizing.ds.on.Single<Throwable>(){
                            @Override public void on(Throwable e) {
                                Log.d(Tag, "fail");
                                __callback.error(e);
                                __callback.complete();
                            }
                        });
            }
        });
    }


}
