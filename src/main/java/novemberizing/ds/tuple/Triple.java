package novemberizing.ds.tuple;

/**
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public class Triple<A, B, C> extends Pair<A, B> {
    public C third;

    public Triple(){
        super();
        this.third = null;
    }

    public Triple(A first, B second, C third){
        super(first, second);
        this.third = third;
    }
}
