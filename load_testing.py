from locust import HttpUser, task, between

class MyUser(HttpUser):
    wait_time = between(1, 5)  # Wait time between consecutive tasks

    @task
    def get_movies(self):
        self.client.get("/movies")

    @task
    def get_theaters(self):
        self.client.get("/theaters")

    @task
    def get_showings(self):
        self.client.get("/showings")

    @task
    def get_specific_movie(self):
        movie_id = "tt0320691"  
        self.client.get(f"/movies/{movie_id}")
    
    @task
    def get_movie_filtering(self):
        filter = "customerRating=5.5&mpaRating=PG"
        self.client.get(f"/movies?{filter}")

    @task
    def get_specific_theater(self):
        theater_id = 1  
        self.client.get(f"/theaters/{theater_id}")

    @task
    def get_theater_showings(self):
        theater_id = 1  
        self.client.get(f"/theaters/{theater_id}/showings")
        
    @task
    def login(self):
        
        email = "jDoe@gmail.com"
        password = "pass1"
        response = self.client.post("/login", params={"email": email, "password": password})
        if response.status_code == 200:
            self.auth_token = response.text
        else:
            self.auth_token = None
    
    def get_token(self):
        # Define the login payload
        login_payload = {
            "email": "jSmith@gmail.com",
            "password": "pass2"
        }
        # Send a POST request to the login endpoint
        response = self.client.post("/login", params=login_payload)
        if response.status_code == 200:
            return response.text
        else:
            return None
        

    @task
    def purchase_tickets(self):
        # Get authorization token as it may need to be refreshed/regenerated
        auth_token = self.get_token()
        
        headers = {'Authorization': f'Bearer {auth_token}'}
        showing_id = 3 
        num_tickets = 2 
        response = self.client.put(f"/showings/{showing_id}?numberTickets={num_tickets}", headers=headers)
        
        if response.status_code == 200:
            # Successfully purchased tickets
            pass
        elif response.status_code == 304:
            # Not enough tickets available
            pass
        else:
            # Showing not found 
            pass
