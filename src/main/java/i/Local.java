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
    @Expose public T in;
    @Expose protected Object out;

    public Object get(int it){
        if(it== IN){
            return in;
        } else if(it== IN +1 || it== IN +2){
            return out;
        }
        return null;
    }

    public <U> U get(int it, Class<U> c) throws ClassCastException {
        if(it== IN){
            return c.cast(in);
        } else if(it== IN +1 || it== IN +2){
            return c.cast(out);
        }
        return null;
    }

    public void set(int it, Object in, Object out){
        Log.f("", this, it, in, out);
        this.in = (T) in;
        this.out = out;
    }

    public Object out(){ return out; }

    public <U> U out(Class<U> c){
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
