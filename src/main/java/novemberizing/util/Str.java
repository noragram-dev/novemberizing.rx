package novemberizing.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 2. 7.
 */
@SuppressWarnings("unused")
public class Str {
    public static String From(BufferedReader reader){
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine())!=null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
