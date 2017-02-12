package novemberizing.ds.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 2. 12.
 */
public class Quintuple<A, B, C, D, E> extends Quadruple<A, B, C, D> {
    @Expose public E fifth;

    public Quintuple(){
        super();
        this.fifth = null;
    }

    public Quintuple(A first, B second, C third, D fourth, E fifth){
        super(first, second, third, fourth);
        this.fifth = fifth;
    }
}
