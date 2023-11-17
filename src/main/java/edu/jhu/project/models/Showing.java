package edu.jhu.project.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Showing
{
    private int id;
	private String movieId;
	private int theaterId;
	private double price;
	private String format;
	private LocalDate date;
	private LocalTime time;
	private int totalTickets;
	private int remainingTickets;
    private Map<String, Object> additionalMetaData;

    public Showing(int id, String movieId, int theaterId, double price, String format, LocalDate date, LocalTime time, int totalTickets, int remainingTickets)
    {
        super();
        this.id = id;
        this.movieId = movieId;
		this.theaterId = theaterId;
		this.price = price;
		this.format = format;
		this.date = date;
		this.time = time;
		this.totalTickets = totalTickets;
		this.remainingTickets = remainingTickets;
		this.additionalMetaData = new HashMap<String, Object>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    public int getTheaterId()
    {
        return theaterId;
    }

    public void setTheaterId(int theaterId)
    {
        this.theaterId = theaterId;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public int getTotalTickets()
    {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets)
    {
        this.totalTickets = totalTickets;
    }

    public int getRemainingTickets()
    {
        return remainingTickets;
    }

    public void setRemainingTickets(int remainingTickets)
    {
        this.remainingTickets = remainingTickets;
    }

    public void addMetaData(String key, Object value){
        additionalMetaData.put(key, value);
    }

    public void removeMetaData(String key) {
        additionalMetaData.remove(key);
    }

    public Object getMetaData(String key) {
        return additionalMetaData.get(key);
    }
}