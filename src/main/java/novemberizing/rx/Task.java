package novemberizing.rx;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Task<T, Z> extends novemberizing.ds.Task {
    @Expose public final T in;
    @Expose public Z out;

    public Task(T in) {
        this.in = in;
        this.out = null;
    }

    protected Task(T in, Z out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() {
        complete();
    }
}
