package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;

import com.jparams.store.comparison.Comparison;

/**
 * Comparison strategy for comparing only the local time element of a local date tome
 */
public class LocalTimeComparison implements Comparison<LocalDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == LocalDateTime.class;
    }

    @Override
    public Object createComparable(final LocalDateTime localDateTime)
    {
        return localDateTime.toLocalTime();
    }
}
