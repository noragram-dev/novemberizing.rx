package novemberizing.rx;

import java.util.Collection;
import java.util.LinkedList;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Replayer<T> {

    protected interface Play<T> { void play(Observer<T> observer); }

    protected class Emits<T> implements Play<T>  {
        private LinkedList<T> __list = new LinkedList<>();
        private T __last = null;

        public Emits(Collection<T> o){
            __list.addAll(o);
            __last = __list.getLast();
        }

        public T last(){ return __last; }

        @Override
        public void play(Observer<T> observer) {
            Scheduler scheduler = observer.observeOn();
            if(Scheduler.Self()==scheduler) {
                try {
                    for(T o : __list) {
                        observer.onNext(o);
                    }
                } catch(Exception e){
                    observer.onError(e);
                }
            } else {
                scheduler.dispatch(new Subscriber.Observes<>(__list, observer));
            }
        }
    }

    protected class Emit<T> implements Play<T> {
        private final T __o;

        public Emit(T o){
            __o = o;
        }

        @Override
        public void play(Observer<T> observer) {
            Scheduler scheduler = observer.observeOn();
            if(Scheduler.Self()==scheduler) {
                try {
                    observer.onNext(__o);
                } catch(Exception e){
                    observer.onError(e);
                }
            } else {
                scheduler.dispatch(new Subscriber.Observe<>(__o, observer));
            }
        }
    }

    protected class Error<T> implements Play<T> {
        private final Throwable __e;

        public Error(Throwable e){
            __e = e;
        }

        @Override
        public void play(Observer<T> observer) {
            Scheduler scheduler = observer.observeOn();
            if(Scheduler.Self()==scheduler) {
                try {
                    observer.onError(__e);
                } catch(Exception e){
                    observer.onError(e);
                }
            } else {
                scheduler.dispatch(new Subscriber.Error<>(__e, observer));
            }
        }
    }

    protected class Complete<T> implements Play<T> {
        private final T __o;

        public Complete(T o){
            __o = o;
        }

        @Override
        public void play(Observer<T> observer) {
            Scheduler scheduler = observer.observeOn();
            if(Scheduler.Self()==scheduler) {
                try {
                    observer.onComplete();
                } catch(Exception e){
                    observer.onError(e);
                }
            } else {
                scheduler.dispatch(new Subscriber.Complete<>(__o, observer));
            }
        }
    }

    private final LinkedList<Play<T>> __replays = new LinkedList<>();
    private int __limit;
    private T __last;


    public T last() { return __last; }

    public void limit(int limit) {
        __limit = limit;
        if(__limit<0){ __limit = Infinite; }
        while(__limit < __replays.size()){ __replays.pollFirst(); }
    }

    public void add(T o){
        if(__limit!=0){
            __replays.addLast(new Emit<>(o));
            __last = o;
        }
        if(__limit!=Infinite && __limit<__replays.size()){
            __replays.pollFirst();
        }
    }

    public void all(Collection<T> o){
        if(__limit!=0){
            Emits<T> emits = new Emits<>(new LinkedList<>(o));
            __replays.addLast(emits);
            __last = emits.last();
        }
        if(__limit!=Infinite && __limit<__replays.size()){
            __replays.pollFirst();
        }
    }

    public void error(Throwable e){
        if(__limit!=0){
            __replays.addLast(new Error<>(e));
        }
        if(__limit!=Infinite && __limit<__replays.size()){
            __replays.pollFirst();
        }
    }

    public void complete(T current){
        if(__limit!=0){
            __replays.addLast(new Complete<>(current));
        }
        if(__limit!=Infinite && __limit<__replays.size()){
            __replays.pollFirst();
        }
    }


    public void replay(Observer<T> observer){
        for(Play<T> o : __replays){
            o.play(observer);
        }
    }

    public Replayer(int limit){
        __limit = limit;
        if(__limit<0){ __limit = Infinite; }
    }
}
