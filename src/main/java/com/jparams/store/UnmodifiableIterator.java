//
// Copyright (c) 2018 Resonate Group Ltd.  All Rights Reserved.
//

package com.jparams.store;

import java.util.Iterator;

public class UnmodifiableIterator<V> implements Iterator<V>
{
    private final Iterator<V> iterator;

    public UnmodifiableIterator(final Iterator<V> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    @Override
    public V next()
    {
        return iterator.next();
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("remove");
    }
}
