package com.jparams.store.comparison.date.time;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.jparams.store.comparison.Comparison;

/**
 * Comparison strategy for comparing only the local time element of a local date tome
 */
public class OffsetTimeComparison implements Comparison<OffsetDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == OffsetDateTime.class;
    }

    @Override
    public Object createComparable(final OffsetDateTime offsetDateTime)
    {
        return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime();
    }
}
