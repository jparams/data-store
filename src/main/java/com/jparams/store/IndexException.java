//
// Copyright (c) 2018 Resonate Group Ltd.  All Rights Reserved.
//

package com.jparams.store;

import java.util.Collection;

/**
 * Thrown when one or more items added to a Store could not be indexed.
 */
public class IndexException extends RuntimeException
{
    public IndexException(final String message, final Collection<? extends Throwable> suppressedExceptions)
    {
        super(message, null, true, true);
        suppressedExceptions.forEach(this::addSuppressed);
    }
}
