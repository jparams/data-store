package com.jparams.store.reference;

public interface ReferenceFactory<T>
{
    Reference<T> createReference(T obj);
}
