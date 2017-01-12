package novemberizing.rx;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 12.
 */
@SuppressWarnings("unused")
public class Req<T> extends Task<T> {
    public Req(T in, Scheduler scheduler) {
        super(in, scheduler);
    }

    public Req(T in, Scheduler scheduler, Task<?> previous) {
        super(in, scheduler, previous);
    }

    public Req(T in, Operator<T, ?> op, Scheduler scheduler) {
        super(in, op, scheduler);
    }

    public Req(T in, Operator<T, ?> op, Scheduler scheduler, Task<?> previous) {
        super(in, op, scheduler, previous);
    }
}
