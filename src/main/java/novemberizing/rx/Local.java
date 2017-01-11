package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.util.Debug;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Local<T> {
    private static final String Tag = "Local";
    @Expose public T in;
    @Expose public Object out;

    synchronized public void set(int it, Object in, Object out){
        this.in = (T) in;
        this.out = out;
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
        this.out = null;
    }
}
