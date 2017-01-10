package i;

import com.google.gson.annotations.Expose;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import static i.Iteration.IN;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Local<T> {
    private static final String Tag = "Local";
    @Expose protected Task<T> task;
    @Expose public T in;
    @Expose protected Object out;
    @Expose protected int next;

    synchronized public void out(Object o){
        out = o;
    }

    public Object o(){ return out; }

    public <U> U o(Class<U> c){
        try {
            return c.cast(out);
        } catch(ClassCastException e){
            Debug.On(e);
        }
        return null;
    }

    public Local(T in){
        Log.f(Tag, "");
        this.in = in;
        this.next = IN;
        this.out = null;
    }
}
