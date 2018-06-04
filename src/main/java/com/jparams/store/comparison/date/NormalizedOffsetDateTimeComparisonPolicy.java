package com.jparams.store.comparison.date;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.jparams.store.comparison.ComparisonPolicy;

/**
 * Comparison policy for comparing two {@link OffsetDateTime} values normalized to an UTC time offset
 */
public class NormalizedOffsetDateTimeComparisonPolicy implements ComparisonPolicy<OffsetDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == OffsetDateTime.class;
    }

    @Override
    public Object createComparable(final OffsetDateTime offsetDateTime)
    {
        return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
