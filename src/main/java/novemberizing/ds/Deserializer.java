package novemberizing.ds;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 2. 14.
 */
public interface Deserializer<T, Z> { Z deserialize(T in); }
