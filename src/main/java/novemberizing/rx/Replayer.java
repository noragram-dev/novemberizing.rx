package novemberizing.rx;

import java.util.LinkedList;

import static novemberizing.Constant.Infinite;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public class Replayer<T> {
    private static class Replay<T> implements Play<T> {
        private T __o;
        @Override
        public void on(Player<T> player) {
            player.onNext(__o);
        }

        public Replay(T o){ __o = o; }
    }

    private static class Error<T> implements Play<T> {
        private Throwable __e;
        @Override
        public void on(Player<T> player) {
            player.onError(__e);
        }

        public Error(Throwable e){ __e = e; }
    }

    private static class Complete<T> implements Play<T> {
        @Override
        public void on(Player<T> player) {
            player.onComplete();
        }
    }

    private LinkedList<Play<T>> __plays = new LinkedList<>();
    private int __limit;

    public Replayer(){ __limit = Infinite; }
    public Replayer(int limit){ __limit = limit; }

    synchronized public void limit(int limit){
        __limit = limit;
    }

    public void add(T o){
        __add(new Replay<>(o));
    }

    public void error(Throwable e){
        __add(new Error<>(e));
    }

    public void complete(){
        __add(new Complete<>());
    }

    synchronized private void __add(Play<T> play){
        __plays.addLast(play);
        if(__limit!=Infinite && __limit<__plays.size()){
            __plays.pollFirst();
        }
    }

    public void on(Player<T> player){
        for(Play<T> play : __plays){
            play.on(player);
        }
    }
}
