package com.jparams.store;

/**
 * Thrown by an Index when indexing of a new item fails.
 */
class IndexCreationException extends Exception
{
    IndexCreationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
