package com.jparams.store;

@FunctionalInterface
public interface Transformer<V, K>
{
    /**
     * Transform value to one or more keys
     *
     * @param value
     * @return key
     */
    Keys<K> transform(V value);
}
