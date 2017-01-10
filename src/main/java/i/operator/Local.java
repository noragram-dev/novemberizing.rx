package i.operator;

import com.google.gson.annotations.Expose;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Local<T> {
    private static final String Tag = "Local";
    @Expose public T in;
    @Expose public Object out;

    public Local(T in){
        Log.f(Tag, "");
        this.in = in;
        this.out = null;
    }
}
