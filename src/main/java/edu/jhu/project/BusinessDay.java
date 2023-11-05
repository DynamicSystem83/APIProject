package edu.jhu.project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessDay
{
	private DayOfWeek day;
	private LocalTime openTime;
	private LocalTime closeTime;

    public BusinessDay(DayOfWeek day, LocalTime openTime, LocalTime closeTime)
    {
        super();
        this.day = day;
        this.openTime = openTime;
		this.closeTime = closeTime;
    }

    public DayOfWeek getDay()
    {
        return day;
    }

    public void setDay(DayOfWeek day)
    {
        this.day = day;
    }

    public LocalTime getOpenTime()
    {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime)
    {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime()
    {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime)
    {
        this.closeTime = closeTime;
    }
}