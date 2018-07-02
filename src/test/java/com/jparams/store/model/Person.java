package com.jparams.store.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person implements Serializable
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String ipAddress;

    public Person()
    {

    }

    @JsonCreator
    public Person(@JsonProperty("id") final Long id,
                  @JsonProperty("firstName") final String firstName,
                  @JsonProperty("lastName") final String lastName,
                  @JsonProperty("email") final String email,
                  @JsonProperty("gender") final Gender gender,
                  @JsonProperty("ipAddress") final String ipAddress)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.ipAddress = ipAddress;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
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

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(final Gender gender)
    {
        this.gender = gender;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString()
    {
        return "Person{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", email='" + email + '\''
            + ", gender=" + gender
            + ", ipAddress='" + ipAddress + '\''
            + '}';
    }
}
