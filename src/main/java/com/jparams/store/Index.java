package com.jparams.store;

import java.util.List;
import java.util.Optional;

/**
 * Index applied to a {@link Store}
 *
 * @param <T> item type
 */
public interface Index<T>
{
    /**
     * Get first indexed item matching key. This is the same as {@link Index#findFirst(Object)}, but returns an null instead of an optional if no result found.
     *
     * @param key indexed key to lookup
     * @return optional
     */
    default T getFirst(final Object key)
    {
        return findFirst(key).orElse(null);
    }

    /**
     * Find first indexed item matching key. This is the same as {@link Index#getFirst(Object)}, but returns an optional instead of a null if no result found.
     *
     * @param key indexed key to lookup
     * @return optional
     */
    Optional<T> findFirst(final Object key);

    /**
     * Find all indexed items matching key
     *
     * @param key indexed key to lookup
     * @return matching items
     */
    List<T> get(final Object key);

    /**
     * Get name of index
     *
     * @return index names
     */
    String getName();
}
