package com.example.be.repositories;

import com.example.be.dto.MovieDTO;
import com.example.be.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByStatus(String type);

    Movie findByIdAndStatus(int id, String status);

    Movie findById(int id);

    List<Movie> findAll();

    @Query("SELECT new com.example.be.dto.MovieDTO(m.id, m.nameVn, m.director, m.actor, m.country_name_vn, m.type_name_vn, m.release_date, m.end_date, m.brief_vn, m.image, m.trailer, m.status, m.ratings, m.time, m.limitage_vn, m.sort_order) " +
            "FROM Movie m")
    List<MovieDTO> findAllMovieDTO();

    Movie findByIdAndStatusNot(int id, String status);

    List<Movie> findByStatusIsNot(String status);

    List<Movie> findByNameVnContainingIgnoreCase(String nameVn);
}
