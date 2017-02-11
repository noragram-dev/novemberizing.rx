package novemberizing.ds.tuple;

/**
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public class Quadruple<A, B, C, D> extends Triple<A, B, C> {
    public D fourth;

    public Quadruple(){
        super();
        this.fourth = null;
    }

    public Quadruple(A first, B second, C third, D fourth){
        super(first, second, third);
        this.fourth = fourth;
    }
}
