package edu.jhu.project.models;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Movie
{
    private String movieId;
	private String title;
	private String genre;
	private String mpaRating;
	private double customerRating;
	private String selfRef;
	private String imdbRef;

    public Movie(String movieId, String title, String genre, String mpaRating, double customerRating, String selfRef, String imdbRef)
    {
        super();
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.mpaRating = mpaRating;
        this.customerRating = customerRating;
		this.selfRef = selfRef;
		this.imdbRef = imdbRef;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getMpaRating()
    {
        return mpaRating;
    }

    public void setMpaRating(String mpaRating)
    {
        this.mpaRating = mpaRating;
    }

    public double getCustomerRating()
    {
        return customerRating;
    }

    public void setCustomerRating(double customerRating)
    {
        this.customerRating = customerRating;
    }

    public String getSelfRef()
    {
        return selfRef;
    }

    public void setSelfRef(String selfRef)
    {
        this.selfRef = selfRef;
    }

    public String getImdbRef()
    {
        return imdbRef;
    }

    public void setImdbRef(String imdbRef)
    {
        this.imdbRef = imdbRef;
    }
}