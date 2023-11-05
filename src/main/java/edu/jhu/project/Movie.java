package edu.jhu.project;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Movie
{
    private String movieId;
	private String title;
	private String genre;
	private String mpaRating;
	private double customerRating;

    public Movie(String movieId, String title, String genre, String mpaRating, double customerRating)
    {
        super();
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.mpaRating = mpaRating;
        this.customerRating = customerRating;
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
}