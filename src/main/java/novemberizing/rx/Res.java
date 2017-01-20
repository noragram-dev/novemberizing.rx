package novemberizing.rx;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public class Res<T> {
    private static final String Tag = "Res";
    private T __out;
    private Throwable __exception;
    private boolean __completed;

    public Res(){
        __out = null;
        __exception = null;
        __completed = false;
    }

    public T out(){ return __out; }
    public Throwable exception(){ return __exception; }
    public boolean completed(){ return __completed; }

    public void next(T o){
        if(!__completed) {
            __out = o;
        } else {
            Log.e(Tag, "");
        }
    }

    public void error(Throwable e){
        __exception = e;
    }

    public void complete(){
        __completed = true;
    }
}
