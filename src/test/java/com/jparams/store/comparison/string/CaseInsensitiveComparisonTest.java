package com.jparams.store.comparison.string;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CaseInsensitiveComparisonTest
{
    private CaseInsensitiveComparison subject;

    @Before
    public void setUp()
    {
        subject = new CaseInsensitiveComparison();
    }

    @Test
    public void testSupports()
    {
        assertThat(subject.supports(String.class)).isTrue();
        assertThat(subject.supports(Object.class)).isFalse();
    }

    @Test
    public void testGetComparable()
    {
        assertThat(subject.getComparable("AbCd")).isEqualTo("abcd");
    }
}