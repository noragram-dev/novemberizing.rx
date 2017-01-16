package novemberizing.rx.operators;

import novemberizing.ds.Executable;
import novemberizing.rx.Operator;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class CompletionPort extends Operator<Executable,Executable> {
    private static final String Tag = "CompletionPort";

    @Override
    protected void on(Operator.Local<Executable, Executable> task) {
    }

    public static class Local extends Operator.Local<Executable, Executable> implements novemberizing.ds.CompletionPort {

        public Local(Executable in, Executable out, Operator<Executable, Executable> op) {
            super(in, out, op);
            in.add(this);
        }

        @Override
        public void dispatch(Executable executable) {
            Log.f(Tag, this, executable);
            done(out = executable);
        }
    }

    public static class Internal extends Operator.Internal<Executable, Executable> {

        @Override
        protected Operator.Local<Executable, Executable> exec(Executable o){
            Local task = new Local(o, null, parent);

            Log.i(Tag, o);

            __observableOn.dispatch(task);

            return task;
        }

        public Internal(Operator<Executable, Executable> p) {
            super(p);
        }

    }

    @Override
    protected Operator.Internal<Executable, Executable> initialize(){
        return internal = new Internal(this);
    }
}
