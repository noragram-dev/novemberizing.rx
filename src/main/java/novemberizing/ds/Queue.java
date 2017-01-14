package novemberizing.ds;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Queue<T> {
    private LinkedList<T> __q = new LinkedList<>();
    private Lock __sync;
    private Condition __condition;

    public Queue(){
        __sync = new ReentrantLock();
        __condition = __sync.newCondition();
    }

    public Queue(Lock sync){
        __sync = sync;
        __condition = __sync.newCondition();
    }

    public Queue(Lock sync, Condition condition){
        __sync = sync;
        __condition = condition;
    }

    public void lock(){ __sync.lock(); }
    public void unlock(){ __sync.unlock(); }

    public void suspend(){ suspend(Infinite); }

    public void suspend(long nano){
        if(nano<=0){
            try {
                __condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                __condition.awaitNanos(nano);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume(){ resume(false); }

    public void resume(boolean all){
        if(all){
            __condition.signalAll();
        } else {
            __condition.signal();
        }
    }

    public final T front(){ return __q.pollFirst(); }
    public final T back(){ return __q.pollLast(); }
    public final void front(T o){ __q.addLast(o); }
    public final void back(T o){ __q.addFirst(o); }

    public void push(T o){ __q.addLast(o); }
    public T pop(){ return __q.pollFirst(); }

    public int size(){ return __q.size(); }
}
