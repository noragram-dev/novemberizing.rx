package novemberizing.ds;

import com.google.gson.annotations.Expose;

import static novemberizing.Constant.Unknown;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 12
 */
public class exception extends Throwable {
    @Expose private int __id;
    public int id(){ return __id; }

    public exception(){
        __id = Unknown;
    }

    public exception(int id){
        __id = id;
    }

    public exception(String msg){
        super(msg);
        __id = Unknown;
    }

    public exception(int id, String msg){
        super(msg);
        __id = id;
    }
}
