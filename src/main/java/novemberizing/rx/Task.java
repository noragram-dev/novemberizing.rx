package novemberizing.rx;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Task<T, U> extends novemberizing.ds.Task {
    @Expose public final T in;
    @Expose public U out;

    public Task(T in) {
        this.in = in;
        this.out = null;
    }

    protected Task(T in, U out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() {

    }
}
