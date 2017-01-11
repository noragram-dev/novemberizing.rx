package novemberizing.rx.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public class Triple<A, B, C> extends novemberizing.rx.tuple.Pair<A, B> {
    @Expose public C third;
    public Triple(){
        super();
        third = null;
    }
    public Triple(A first, B second, C third){
        super(first, second);
        this.third = third;
    }
}
