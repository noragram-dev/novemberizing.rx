package novemberizing.rx;

import novemberizing.util.Log;

import java.util.Collection;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 16
 */
public class Tasks extends Task<Collection<Task>, Collection<Task>> {
    private static final String Tag = "Tasks";

    public final Tasks __self = this;

    public Tasks(Collection<Task> in, Collection<Task> out) {
        super(in, out);
    }

    public void add(Task task){
        if(task!=null) {
            if(!task.completed()) {
                task.append(new Subscribers.Just<Task>() {
                    @Override
                    public void onNext(Task task) {
                        Log.e(Tag, task);
                        synchronized (__self) {
                            if (task.completed()) {
                                out.add(task);
                                if (in.size() == out.size()) {
                                    __completed = true;
                                    complete();
                                }
                            }
                        }
                        onUnsubscribe(task.__observable);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(Tag, this);
                        synchronized (__self) {
                            out.add(task);
                            if (in.size() == out.size()) {
                                __completed = true;
                                complete();
                            }
                        }
                        onUnsubscribe(task.__observable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Tag, this);
                        Log.e(Tag, e);
                        synchronized (__self) {
                            out.add(task);
                            if (in.size() == out.size()) {
                                __completed = true;
                                complete();
                            }
                        }
                        onUnsubscribe(task.__observable);
                    }
                });
            } else {
                synchronized (__self) {
                    out.add(task);
                    if (in.size() == out.size()) {
                        Log.i(Tag, "out: " + out.size());
                        __completed = true;
                        complete();
                    }
                }
            }
        }
    }
}
