package com.jparams.store.comparison.date.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jparams.store.comparison.Comparison;

/**
 * Comparison strategy for comparing that two ZonedDateTime’s are at same moment in time regardless of time zones.
 */
public class ZonedDateTimeComparison implements Comparison<ZonedDateTime>
{

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == ZonedDateTime.class;
    }

    @Override
    public Object createComparable(final ZonedDateTime item)
    {
        return item.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }
}
