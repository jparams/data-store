package com.jparams.store.index;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.Store;

/**
 * Index applied to a {@link Store}
 *
 * @param <V> value type
 */
public interface Index<V>
{
    /**
     * Get first indexed item matching key. This is the same as {@link Index#findFirst(Object)}, but returns an null instead of an optional if no result found.
     *
     * @param key indexed key to lookup
     * @return optional
     */
    default V getFirst(final Object key)
    {
        return findFirst(key).orElse(null);
    }

    /**
     * Find first indexed item matching key. This is the same as {@link Index#getFirst(Object)}, but returns an optional instead of a null if no result found.
     *
     * @param key indexed key to lookup
     * @return optional
     */
    Optional<V> findFirst(final Object key);

    /**
     * Find all indexed items matching key
     *
     * @param key indexed key to lookup
     * @return matching items
     */
    List<V> get(final Object key);

    /**
     * Get name of index
     *
     * @return index names
     */
    String getName();

    /**
     * Return all keys held by the index
     *
     * @return keys
     */
    Set<Object> getKeys();

    /**
     * Return all entries in index
     *
     * @return entry of key and values
     */
    default List<Entry<V>> getEntries()
    {
        return getKeys().stream().map(key -> new Entry<>(key, this)).collect(Collectors.toList());
    }
}
