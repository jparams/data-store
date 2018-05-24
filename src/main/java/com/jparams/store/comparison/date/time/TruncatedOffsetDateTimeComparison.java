package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.jparams.store.comparison.Comparison;
import com.jparams.store.comparison.date.TruncateDateTime;

/**
 * Comparison strategy for comparing only the local date element of a local date tome
 */
public class TruncatedOffsetDateTimeComparison implements Comparison<OffsetDateTime>
{
    private final TruncateDateTime truncatedTo;

    public TruncatedOffsetDateTimeComparison(final TruncateDateTime truncatedTo)
    {
        this.truncatedTo = truncatedTo;
    }

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == OffsetDateTime.class;
    }

    @Override
    public Object createComparable(final OffsetDateTime offsetDateTime)
    {
        final LocalDateTime localDateTime = offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();

        return LocalDateTime.of(offsetDateTime.getYear(),
                                truncatedTo.getMonth().orElse(localDateTime.getMonthValue()),
                                truncatedTo.getDay().orElse(localDateTime.getDayOfMonth()),
                                truncatedTo.getHour().orElse(localDateTime.getHour()),
                                truncatedTo.getMinute().orElse(localDateTime.getMinute()),
                                truncatedTo.getSecond().orElse(localDateTime.getSecond()),
                                truncatedTo.getMillis().orElse(localDateTime.getNano()));
    }
}
