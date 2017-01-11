package novemberizing.ds;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public class queue<T> {
    protected Lock __sync;
    protected Condition __condition;
    protected LinkedList<T> __q = new LinkedList<>();

    public T front(){ return __q.size()>0 ? __q.peekFirst() : null; }
    public T back(){ return __q.size()>0 ? __q.peekLast() : null; }

    public void back(T o){ __q.addLast(o); }
    public void front(T o){ __q.addFirst(o); }

    public T pop(){ return __q.pop(); }
    public void push(T o){ __q.push(o); }

    public int size(){ return __q.size(); }

    public void lock(){ __sync.lock(); }
    public void unlock(){ __sync.unlock(); }

    public Lock sync(){ return __sync; }
    public Condition condition(){ return __condition; }

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

    public void resume(boolean all){
        if(all){
            __condition.signalAll();
        } else {
            __condition.signal();
        }
    }

    public queue(){
        __sync = new ReentrantLock();
        __condition = __sync.newCondition();
    }

    public queue(Lock sync){
        __sync = sync;
        __condition = __sync.newCondition();
    }

    public queue(Lock sync, Condition condition){
        __sync = sync;
        __condition = condition;
    }
}
