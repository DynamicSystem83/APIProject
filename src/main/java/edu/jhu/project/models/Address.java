package edu.jhu.project.models;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Address
{
    private String street;
    private String city;
	private String state;

    public Address(String street, String city, String state)
    {
        super();
        this.street = street;
        this.city = city;
		this.state = state;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }
}