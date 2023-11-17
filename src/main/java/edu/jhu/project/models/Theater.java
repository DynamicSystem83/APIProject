package edu.jhu.project.models;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Theater
{
    private int theaterId;
    private String name;
	private Address addr;
	private List<BusinessDay> businessHours;

    public Theater(int theaterId, String name, Address addr, List<BusinessDay> businessHours)
    {
        super();
        this.theaterId = theaterId;
        this.name = name;
		this.addr = addr;
		this.businessHours = businessHours;
    }

    public int getTheaterId()
    {
        return theaterId;
    }

    public void setTheaterId(int theaterId)
    {
        this.theaterId = theaterId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Address getAddr()
    {
        return addr;
    }

    public void setAddr(Address addr)
    {
        this.addr = addr;
    }

    public List<BusinessDay> getBusinessHours()
    {
        return businessHours;
    }

    public void setBusinessHours(List<BusinessDay> businessHours)
    {
        this.businessHours = businessHours;
    }
}