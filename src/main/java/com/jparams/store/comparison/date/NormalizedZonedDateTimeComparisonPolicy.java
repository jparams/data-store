package com.jparams.store.comparison.date;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.jparams.store.comparison.ComparisonPolicy;

/**
 * Comparison policy for comparing two {@link ZonedDateTime} values normalized to a UTC zone offset
 */
public class NormalizedZonedDateTimeComparisonPolicy implements ComparisonPolicy<ZonedDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == ZonedDateTime.class;
    }

    @Override
    public Object createComparable(final ZonedDateTime zonedDateTime)
    {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
