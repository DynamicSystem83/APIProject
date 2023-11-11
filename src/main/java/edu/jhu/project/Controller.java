package edu.jhu.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import java.lang.IllegalArgumentException;
import java.util.stream.Collectors;  

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Caffeine;
import org.springframework.cache.annotation.Cacheable;
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
		// TODO: Add caching


		List<Movie> movieList = getMovieListFromIds(movies);

		// Create a predicate to filter based on request parameter
		Predicate<Movie> filterMoviePredicate = movie -> {
            // Filter by genre if provided
            if (genre != null && movie.getGenre() != genre) {
                return false;
            }

            // Filter by mpaRating if provided
			if (mpaRating != null && movie.getMpaRating() != mpaRating) {
                return false;
            }
			
			// Filter by title 
			if (title != null && movie.getTitle() != title) {
                return false;
            }
			// Filter by customerRating if provided
			if (customerRating != 0.0d && movie.getCustomerRating() < customerRating) {
                return false;
            }
            return true;
        };

		List<Movie> filteredMovies= movieList.stream()
                .filter(filterMoviePredicate)
                .collect(Collectors.toList());
		
		if (filteredMovies.isEmpty()) {
			// TODO: Should this return an error if empty or just return empty list??
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid query request.");
		}

        return ResponseEntity.ok(filteredMovies);
    }

	@GetMapping(value = "/movies/{movieId}")
    public ResponseEntity getSpecificMovie(@PathVariable String movieId)
    {
		Movie m = getMovieFromId(movieId);
		if (m != null)
		{
			return ResponseEntity.ok(m);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
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
		Theater currentTheater = findTheaterById(theaterId);
		if (currentTheater == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theater not found");
		}
		return ResponseEntity.ok(currentTheater);
	}

	@GetMapping(value = "/theaters/{theaterId}/showings")
	public ResponseEntity getSpecificTheaterShowings(@PathVariable int theaterId)
	{
		Theater currentTheater = findTheaterById(theaterId);
		if (currentTheater == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theater not found");
		}

		List<Showing> showingsForTheater = findShowingsByTheaterId(theaterId);

		for (Showing s : showingsForTheater){
			s.addMetaData("Movie", getMovieFromId(s.getMovieId()));
		}

		// Create a Map to hold the results
        Map<String, Object> result = new HashMap<>();
		result.put("theater", currentTheater);
        result.put("showings", showingsForTheater);

		// TODO: is the map needed or should just the showings be returned??
		return ResponseEntity.ok(result);	
	}

	@GetMapping(value = "/theaters/{theaterId}/movies")
	public ResponseEntity getSpecificTheaterAvailableMovies(@PathVariable int theaterId)
	{
		Theater currentTheater = findTheaterById(theaterId);
		if (currentTheater == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theater not found");
		}

		List<Showing> showingsForTheater = findShowingsByTheaterId(theaterId);
		List<String> movies = showingsForTheater.stream()
            .map(Showing::getMovieId) // Extract movie IDs from showings
            .distinct() 
            .collect(Collectors.toList()); 

		List<Movie> movieList = getMovieListFromIds(movies);

		// Create a Map to hold the results
        Map<String, Object> result = new HashMap<>();


		result.put("theater", currentTheater);
        result.put("movies", movieList);

		return ResponseEntity.ok(result);	
	}

	@GetMapping(value = "/showings")
    public ResponseEntity getShowings(@RequestParam(value="movieId", required=false) String movieId,
									@RequestParam(value="theaterId", required=false) Integer theaterId,
									@RequestParam(value="date", required=false) String date)
    {
		List<Showing> showingList = new ArrayList<Showing>();

		// Create a predicate to filter based on request parameters
		Predicate<Showing> filterPredicate = showing -> {
            LocalDate currentDate = LocalDate.now();

            // Filter by theaterId if provided
            if (theaterId != null && showing.getTheaterId() != theaterId.intValue()) {
                return false;
            }

            // Filter by date if provided
            if (date != null) {
                LocalDate showingDate = LocalDate.parse(date);
                if (!showing.getDate().isEqual(showingDate)) {
                    return false;
                }
            }

            // Filter current and future showings
            return showing.getDate().isAfter(currentDate) || showing.getDate().isEqual(currentDate);
        };

		List<Showing> filteredShowings = showings.stream()
                .filter(filterPredicate)
                .collect(Collectors.toList());

		if (filteredShowings.isEmpty()) {
			// TODO: Should this return an error if empty or just return empty list??
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid query request.");
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
	
	@Cacheable("movies")
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

	private Theater findTheaterById(int theaterId) {
        // Logic to find and return the theater by ID
        return theaters.stream()
            .filter(theater -> theater.getTheaterId() == theaterId)
            .findFirst()
            .orElse(null); // Handle if theater is not found
    }

	private List<Showing> findShowingsByTheaterId(int theaterId) {
        // Logic to find and return the theater by ID
		LocalDate currentDate = LocalDate.now();
        return showings.stream()
            .filter(showing -> showing.getTheaterId() == theaterId 
			&& (showing.getDate().isAfter(currentDate) || showing.getDate().isEqual(currentDate)))
            .collect(Collectors.toList());
    }
}