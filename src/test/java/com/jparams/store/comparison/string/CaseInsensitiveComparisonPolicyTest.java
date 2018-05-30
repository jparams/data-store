package com.jparams.store.comparison.string;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CaseInsensitiveComparisonPolicyTest
{
    private CaseInsensitiveComparisonPolicy subject;

    @Before
    public void setUp()
    {
        subject = new CaseInsensitiveComparisonPolicy();
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
        assertThat(subject.createComparable("AbCd")).isEqualTo("abcd");
    }
}