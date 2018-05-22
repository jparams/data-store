package com.jparams.store.comparison;

/**
 * Apply default comparison logic
 *
 * @param <V> value type
 */
public class DefaultComparison<V> implements Comparison<V>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return true;
    }

    @Override
    public Object getComparable(final V item)
    {
        return item;
    }
}
