package com.example.be.services;

import com.example.be.dto.MovieDTO;
import com.example.be.models.Movie;
import com.example.be.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDTO> all() {
        return movieRepository.findAllMovieDTO();
    }

    public List<Movie> allMovie() {
        return movieRepository.findAll();
    }

    public Movie get(int id) {
        return movieRepository.findById(id);
    }

    public Movie add(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setNameVn(movieDTO.getNameVn());
        movie.setDirector(movieDTO.getDirector());
        movie.setActor(movieDTO.getActor());
        movie.setCountry_name_vn(movieDTO.getCountry_name_vn());
        movie.setType_name_vn(movieDTO.getType_name_vn());
        movie.setRelease_date(movieDTO.getRelease_date());
        movie.setEnd_date(movieDTO.getEnd_date());
        movie.setBrief_vn(movieDTO.getBrief_vn());
        movie.setImage(movieDTO.getImage());
        movie.setTrailer(movieDTO.getTrailer());
        movie.setStatus("1");
        movie.setRatings(movieDTO.getRatings());
        movie.setTime(movieDTO.getTime());
        movie.setLimitage_vn(movieDTO.getLimitage_vn());
        movie.setSort_order(movieDTO.getSort_order());
        return movieRepository.save(movie);
    }

    public Movie update(int id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id);
        movie.setNameVn(movieDTO.getNameVn());
        movie.setDirector(movieDTO.getDirector());
        movie.setActor(movieDTO.getActor());
        movie.setCountry_name_vn(movieDTO.getCountry_name_vn());
        movie.setType_name_vn(movieDTO.getType_name_vn());
        movie.setRelease_date(movieDTO.getRelease_date());
        movie.setEnd_date(movieDTO.getEnd_date());
        movie.setBrief_vn(movieDTO.getBrief_vn());
        movie.setImage(movieDTO.getImage());
        movie.setTrailer(movieDTO.getTrailer());
        movie.setRatings(movieDTO.getRatings());
        movie.setTime(movieDTO.getTime());
        movie.setLimitage_vn(movieDTO.getLimitage_vn());
        movie.setSort_order(movieDTO.getSort_order());
        return movieRepository.save(movie);
    }

    public Movie delete(int id) {
        Movie movie = movieRepository.findById(id);
        movie.setStatus("0");
        return movieRepository.save(movie);
    }

    public Movie singleMovie(int id) {
        return movieRepository.findById(id);
    }

    public List<Movie> getMoviesByType(String type) {
        return movieRepository.findByStatus(type);
    }


    public List<Movie> searchProducts(String nameVn) {
        List<Movie> result = movieRepository.findByNameVnContainingIgnoreCase(nameVn);
        for (Movie movie : result) {
            movie.setShowTimes(null);
        }
        return result;
    }
}
