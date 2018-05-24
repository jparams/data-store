package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;

import com.jparams.store.comparison.Comparison;

/**
 * Compares the {@link java.time.LocalDate} element of two {@link LocalDateTime}
 */
public class LocalDateComparison implements Comparison<LocalDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == LocalDateTime.class;
    }

    @Override
    public Object createComparable(final LocalDateTime localDateTime)
    {
        return localDateTime.toLocalDate();
    }
}
