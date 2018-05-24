package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jparams.store.comparison.Comparison;
import com.jparams.store.comparison.date.TruncateDateTime;

/**
 * Comparison strategy for comparing that two ZonedDateTime’s are at same moment in time regardless of time zones.
 */
public class TruncatedZonedDateTimeComparison implements Comparison<ZonedDateTime>
{
    private final TruncateDateTime truncatedTo;

    public TruncatedZonedDateTimeComparison(final TruncateDateTime truncatedTo)
    {
        this.truncatedTo = truncatedTo;
    }

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == ZonedDateTime.class;
    }

    @Override
    public Object createComparable(final ZonedDateTime item)
    {
        final LocalDateTime localDateTime = item.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return new TruncatedLocalDateTimeComparison(truncatedTo).createComparable(localDateTime);
    }
}
