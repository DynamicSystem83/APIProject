package edu.jhu.project;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.IllegalArgumentException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@Validated
public class Controller
{
	private List<String> movies = new ArrayList<String>();

	private final AtomicInteger theaterIdCounter = new AtomicInteger();
	private List<Theater> theaters = new ArrayList<Theater>();

	private final AtomicInteger showingIdCounter = new AtomicInteger();
	private List<Showing> showings = new ArrayList<Showing>();
    
	public Controller()
	{
		movies.add("tt0320691");
		movies.add("tt0401855");
		movies.add("tt0884328");
		movies.add("tt0100157");
		movies.add("tt0111161");
		movies.add("tt0076759");
		movies.add("tt0080684");
		movies.add("tt0086190");
		movies.add("tt0087332");
		movies.add("tt1160419");

		List<BusinessDay> businessHours = new ArrayList();
		businessHours.add(new BusinessDay(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(23, 0)));
		businessHours.add(new BusinessDay(DayOfWeek.TUESDAY, LocalTime.of(13, 0), LocalTime.of(23, 0)));
		businessHours.add(new BusinessDay(DayOfWeek.WEDNESDAY, LocalTime.of(13, 0), LocalTime.of(23, 0)));
		businessHours.add(new BusinessDay(DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(23, 0)));
		businessHours.add(new BusinessDay(DayOfWeek.FRIDAY, LocalTime.of(13, 0), LocalTime.of(23, 59)));
		businessHours.add(new BusinessDay(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(23, 59)));
		businessHours.add(new BusinessDay(DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(21, 0)));

		theaters.add(new Theater(theaterIdCounter.incrementAndGet(), "Alamo Drafthouse Village", new Address("2700 W Anderson Ln.", "Austin", "Texas"), businessHours));
		theaters.add(new Theater(theaterIdCounter.incrementAndGet(), "Alamo Drafthouse South Lamar", new Address("1120 S Lamar Blvd.", "Austin", "Texas"), businessHours));
		theaters.add(new Theater(theaterIdCounter.incrementAndGet(), "Alamo Drafthouse LaCenterra", new Address("2707 Commercial Center Blvd.", "Houston", "Texas"), businessHours));
		theaters.add(new Theater(theaterIdCounter.incrementAndGet(), "Marcus Point Cinema", new Address("7825 Big Sky Dr.", "Madison", "Wisconsin"), businessHours));
		theaters.add(new Theater(theaterIdCounter.incrementAndGet(), "AMC Fitchburg 18", new Address("6091 McKee Rd.", "Fitchburg", "Wisconsin"), businessHours));

		showings.add(new Showing(showingIdCounter.incrementAndGet(), "tt0320691", 1, 12.00, "Standard", LocalDate.of(2023, 10, 21), LocalTime.of(15, 15), 20, 7));
		showings.add(new Showing(showingIdCounter.incrementAndGet(), "tt0320691", 1, 12.00, "IMAX", LocalDate.of(2023, 11, 21), LocalTime.of(15, 0), 15, 3));
		showings.add(new Showing(showingIdCounter.incrementAndGet(), "tt0401855", 1, 12.00, "Standard", LocalDate.of(2023, 11, 25), LocalTime.of(14, 0), 15, 3));
		showings.add(new Showing(showingIdCounter.incrementAndGet(), "tt0884328", 2, 15.00, "IMAX", LocalDate.of(2023, 12, 5), LocalTime.of(14, 0), 15, 3));
		showings.add(new Showing(showingIdCounter.incrementAndGet(), "tt0100157", 3, 15.00, "3D", LocalDate.of(2023, 12, 5), LocalTime.of(14, 0), 15, 3));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e)
	{
		return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}


	@GetMapping(value = "/movies")
    public ResponseEntity getMovies(@RequestParam(value="genre", required=false) String genre,
									@RequestParam(value="mpaRating", required=false) String mpaRating,
									@RequestParam(value="customerRating", required=false) double customerRating,
									@RequestParam(value="title", required=false) String title)
    {
		// TODO: Add filtering
		// TODO: Add caching
		// TODO: Add errors
		List<Movie> movieList = getMovieListFromIds(movies);
		if (genre != null)
		{
			movieList = movieList.stream().filter(m -> m.getGenre().equals(genre)).collect(Collectors.toList());
			/*for (Movie m : movieList)
			{
				if (!m.getGenre().equals(genre))
				{
					movieList.remove(m);
				}
			}*/
		}
		if (mpaRating != null)
		{
			movieList = movieList.stream().filter(m -> m.getMpaRating().contains(mpaRating)).collect(Collectors.toList());
			/*for (Movie m : movieList)
			{
				if (!m.getMpaRating().equals(mpaRating))
				{
					movieList.remove(m);
				}
			}*/
		}
		if (customerRating != null)
		{
			movieList = movieList.stream().filter(m -> m.getCustomerRating() >= customerRating).collect(Collectors.toList());
			/*for (Movie m : movieList)
			{
				if (m.getCustomerRating() < customerRating)
				{
					movieList.remove(m);
				}
			}*/
		}
        return ResponseEntity.ok(movieList);
    }

	@GetMapping(value = "/movies/{movieId}")
    public ResponseEntity getSpecificMovie(@PathVariable String movieId)
    {
		Movie m = getMovieFromId(movieId);
		if (m != null)
		{
			return ResponseEntity.ok(m);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Showing not found");
	}

	@GetMapping(value = "/theaters")
    public ResponseEntity getTheaters(@RequestParam(value="city", required=false) String city,
									@RequestParam(value="state", required=false) String state)
    {
        return ResponseEntity.ok(theaters);
	}

	@GetMapping(value = "/theaters/{theaterId}")
    public ResponseEntity getSpecificTheater(@PathVariable int theaterId)
    {
		// TODO: only show future showings
		// TODO: Add errors
		// Should this be changed to also show theater details if a query parameter is set
		List<String> movieIds = new ArrayList<String>();
		for (Showing s : showings)
		{
			if (s.getTheaterId() == theaterId)
			{
				if (s.getDate() > LocalDate.now())
				{
					if (!movieIds.contains(s.getMovieId()))
					{
						movieIds.add(s.getMovieId());
					}
				}
			}
		}
		
		return ResponseEntity.ok(getMovieListFromIds(movieIds));
	}

	@GetMapping(value = "/showings")
    public ResponseEntity getShowings(@RequestParam(value="movieId", required=false) String movieId,
									@RequestParam(value="theaterId", required=false) int theaterId,
									@RequestParam(value="date", required=false) String date)
    {
		// TODO: Add filtering (including only show future showings)
		// TODO: Add errors
		List<Showing> showingList = new ArrayList<Showing>();
		for (Showing s : showings)
		{
			if (s.getDate() > LocalDate.now())
			{
				showingList.add(s);
			}
		}
        return ResponseEntity.ok(showingList);
	}

	@PutMapping(value = "/showings/{showingId}")
    public ResponseEntity purchaseTickets(@PathVariable int showingId,
								  @RequestParam(value="numberTickets", required=true) int numberTickets)
    {
		for (Showing s : showings)
		{
			if (s.getId() == showingId)
			{
				if (s.getRemainingTickets() >= numberTickets)
				{
					s.setRemainingTickets(s.getRemainingTickets()-numberTickets);
					double totalPrice = numberTickets*s.getPrice();
					return ResponseEntity.ok(totalPrice);
				}
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Not enough tickets available");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Showing not found");
    }
	
	public Movie getMovieFromId(String movieId)
	{
		// TODO: Add caching
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		String prefixURL = "http://www.omdbapi.com/?i=";
		String postfixURL = "&apikey=9f6c7222";
		
		ResponseEntity<String> response;
		String respTitle = "";
		String respGenre = "";
		String respMpaRating = "";
		double respCustomerRating = 0.0;

		response = restTemplate.getForEntity(prefixURL + movieId + postfixURL, String.class);

		if (response.getStatusCode() == HttpStatus.OK)
		{
			try
			{
				JsonNode root = mapper.readTree(response.getBody());
				respTitle = root.findValue("Title").textValue();
				respGenre = root.findValue("Genre").textValue();
				respMpaRating = root.findValue("Rated").textValue();
				respCustomerRating = root.findValue("imdbRating").doubleValue();
				return new Movie(movieId, respTitle, respGenre, respMpaRating, respCustomerRating);
			}
			catch (JsonProcessingException e)
			{
				System.out.println("Error: " + e.getMessage());
			}
		}

        return null;
	}
	
	public List<Movie> getMovieListFromIds(List<String> movieIds)
	{
		List<Movie> movieList = new ArrayList<Movie>();
		Movie movie;

		for (String mId : movieIds)
		{
			movie = getMovieFromId(mId);
			if (movie != null)
			{
				movieList.add(movie);
			}
		}
        return movieList;
	}
}