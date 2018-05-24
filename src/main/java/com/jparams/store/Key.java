package com.jparams.store;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Key mappings from a single value
 *
 * @param <K> key type
 */
public final class Key<K>
{
    private final Set<K> values;

    private Key(final Collection<K> values)
    {
        this.values = new HashSet<>(values);
    }

    /**
     * Get all values defined
     *
     * @return list of keyed values
     */
    public Set<K> getValues()
    {
        return Collections.unmodifiableSet(values);
    }

    /**
     * Create empty values
     *
     * @param <K> type
     * @return values
     */
    public static <K> Key<K> none()
    {
        return new Key<>(Collections.emptyList());
    }

    /**
     * Add another key mapping
     *
     * @param key key
     * @param <K> key type
     * @return this
     */
    public static <K> Key<K> on(final K key)
    {
        return new Key<>(Collections.singletonList(key));
    }

    /**
     * Create values with default values
     *
     * @param keys key values
     * @param <K>  key type
     * @return values
     */
    @SafeVarargs
    public static <K> Key<K> onEach(final K... keys)
    {
        return new Key<>(Arrays.asList(keys));
    }

    /**
     * Create values with default values
     *
     * @param keys key values
     * @param <K>  key type
     * @return values
     */
    public static <K> Key<K> onEach(final Collection<K> keys)
    {
        return new Key<>(keys);
    }
}
