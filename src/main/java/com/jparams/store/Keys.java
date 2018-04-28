package com.jparams.store;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Key mappings from a single value
 */
public final class Keys<T>
{
    private final Set<T> keys;

    private Keys(final Collection<T> keys)
    {
        this.keys = new HashSet<>(keys);
    }

    /**
     * Add another key mapping
     *
     * @param key key
     * @return this
     */
    public Keys<T> add(final T key)
    {
        keys.add(key);
        return this;
    }

    /**
     * Get all keys defined
     *
     * @return list of keyed values
     */
    public Set<T> getKeys()
    {
        return Collections.unmodifiableSet(keys);
    }

    /**
     * Create empty keys
     *
     * @param <T> type
     * @return keys
     */
    public static <T> Keys<T> none()
    {
        return new Keys<>(Collections.emptyList());
    }

    /**
     * Create keys with default values
     *
     * @param keys key values
     * @param <T>  type
     * @return keys
     */
    public static <T> Keys<T> create(final T... keys)
    {
        return new Keys<>(Arrays.asList(keys));
    }

    /**
     * Create keys with default values
     *
     * @param keys key values
     * @param <T>  type
     * @return keys
     */
    public static <T> Keys<T> create(final Collection<T> keys)
    {
        return new Keys<>(keys);
    }
}
