package edu.jhu.project;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SiteUser
{
    private int id;
	private String email;
    private String password;
    private String token;

    public SiteUser(int id, String email, String password)
    {
        super();
        this.id = id;
		this.email = email;
		this.password = password;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
	{
        return password;
    }

    public void setPassword(String password)
	{
        this.password = password;
    }

    public String getToken()
	{
        return token;
    }

    public void setToken(String token)
	{
        this.token = token;
    }

    @Override
    public String toString()
	{
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}