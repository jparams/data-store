package com.jparams.store.model;

public final class PersonBuilder
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String ipAddress;

    private PersonBuilder()
    {
    }

    public static PersonBuilder aPerson()
    {
        return new PersonBuilder();
    }

    public PersonBuilder withId(final Long id)
    {
        this.id = id;
        return this;
    }

    public PersonBuilder withFirstName(final String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public PersonBuilder withLastName(final String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder withEmail(final String email)
    {
        this.email = email;
        return this;
    }

    public PersonBuilder withGender(final Gender gender)
    {
        this.gender = gender;
        return this;
    }

    public PersonBuilder withIpAddress(final String ipAddress)
    {
        this.ipAddress = ipAddress;
        return this;
    }

    public Person build()
    {
        final Person person = new Person();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setGender(gender);
        person.setIpAddress(ipAddress);
        return person;
    }
}
