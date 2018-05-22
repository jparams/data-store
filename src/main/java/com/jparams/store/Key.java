package com.jparams.store;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Key mappings from a single value
 */
public final class Key<T>
{
    private final Set<Object> values;

    private Key(final Collection<T> values)
    {
        this.values = new HashSet<>(values);
    }

    /**
     * Get all values defined
     *
     * @return list of keyed values
     */
    public Set<Object> getValues()
    {
        return Collections.unmodifiableSet(values);
    }

    /**
     * Create empty values
     *
     * @param <T> type
     * @return values
     */
    public static <T> Key<T> none()
    {
        return new Key<>(Collections.emptyList());
    }

    /**
     * Add another key mapping
     *
     * @param key key
     * @return this
     */
    public static <T> Key<T> on(final T key)
    {
        return new Key<>(Collections.singletonList(key));
    }

    /**
     * Create values with default values
     *
     * @param keys key values
     * @param <T>  type
     * @return values
     */
    @SafeVarargs
    public static <T> Key<T> onEach(final T... keys)
    {
        return new Key<>(Arrays.asList(keys));
    }

    /**
     * Create values with default values
     *
     * @param keys key values
     * @param <T>  type
     * @return values
     */
    public static <T> Key<T> onEach(final Collection<T> keys)
    {
        return new Key<>(keys);
    }
}
