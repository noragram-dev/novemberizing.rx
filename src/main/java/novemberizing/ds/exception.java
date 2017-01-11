package novemberizing.ds;

import com.google.gson.annotations.Expose;

import static novemberizing.Constant.Unknown;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 12
 */
public class exception extends Throwable {
    @Expose private int id = Unknown;
}
