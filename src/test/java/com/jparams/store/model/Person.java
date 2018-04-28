package com.jparams.store.model;

import java.time.LocalDate;

public class Person
{
    private String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    public Person(final String firstName, final String lastName, final LocalDate dateOfBirth)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    @Override
    public String toString()
    {
        return "Person{firstName='" + firstName + ", lastName='" + lastName + ", dateOfBirth=" + dateOfBirth + '}';
    }
}
