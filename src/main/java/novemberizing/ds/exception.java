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


    public exception(Throwable cause){
        super(cause);
        __id = Unknown;
    }

    public exception(int id){
        __id = id;
    }

    public exception(int id, Throwable cause){
        super(cause);
        __id = id;
    }

    public exception(String msg){
        super(msg);
        __id = Unknown;
    }


    public exception(String msg, Throwable cause){
        super(msg, cause);
        __id = Unknown;
    }

    public exception(int id, String msg){
        super(msg);
        __id = id;
    }

    public exception(int id, String msg, Throwable cause){
        super(msg, cause);
        __id = id;
    }
}
