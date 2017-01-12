package novemberizing.ds;

import novemberizing.util.Log;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Queue<T> {
    private static final String Tag = "Queue";

    private final Lock __sync;
    private final Condition __condition;
    private final LinkedList<T> __q = new LinkedList<>();

    public void lock(){ __sync.lock(); }

    public void unlock(){ __sync.unlock(); }

    public void suspend(long nano){
        if(nano<=0){
            try {
                __condition.await();
            } catch (InterruptedException e) {
                Log.d(Tag, e);
            }
        } else {
            try {
                __condition.awaitNanos(nano);
            } catch (InterruptedException e) {
                Log.d(Tag, e);
            }
        }
    }

    public void resume(boolean all){
        if(all){
            __condition.signalAll();
        } else {
            __condition.signal();
        }
    }

    public T pop(){ return __q.pollFirst(); }

    public T front(){ return __q.pollFirst(); }

    public T back(){ return __q.pollLast(); }

    public void push(T o){ __q.addLast(o); }

    public void front(T o){ __q.addFirst(o); }

    public void back(T o){ __q.addLast(o); }

    public int size(){ return __q.size(); }

    public boolean empty(){ return __q.isEmpty(); }

    public Queue(){
        __sync = null;
        __condition = null;
    }

    public Queue(Lock sync){
        __sync = sync;
        __condition = __sync.newCondition();
    }

    public Queue(Lock sync, Condition condition){
        __sync = sync;
        __condition = condition;
    }
}
