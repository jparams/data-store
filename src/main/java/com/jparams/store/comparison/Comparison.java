package com.jparams.store.comparison;

public interface Comparison<T>
{
    /**
     * Returns true if comparison is supported for this class type
     *
     * @param clazz class to check for support
     * @return true if supported
     */
    boolean supports(Class<?> clazz);

    /**
     * Transform the given item into the comparable type
     *
     * @param item
     * @return comparable
     */
    Object getComparable(T item);
}
