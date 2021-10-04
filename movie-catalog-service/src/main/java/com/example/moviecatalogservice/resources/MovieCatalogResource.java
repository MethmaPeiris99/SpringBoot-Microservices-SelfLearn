package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {


        // Get all rated movie ids
        // ASSUMPTION : ratings list is the response received from ratings-data API
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);

        // Get all movie details from movie-info-service for each movie id
        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

            /**
             * Return an instance of movie with the details consist in passed URL
             */
//            Movie movie = webClientBuilder.build()
//                    .get() //Request type needs to be called
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId()) //URl needs to be accessed
//                    .retrieve() //Indicates that the data is fetched from the URL
//                    .bodyToMono(Movie.class) //Convert the fetched data into an object with the type of the passed object type
//                    .block();

            return new CatalogItem(movie.getMovieName(), "Movie about 6 heroes", rating.getMovieRating());
        }).collect(Collectors.toList());

        // Combine those data altogether
//        return Collections.singletonList(
//                new CatalogItem("Big Hero 6",
//                        "Movie about 6 heroes",
//                        10)
//        );
    }
}
