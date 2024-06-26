package com.example.be.utils;

import com.example.be.models.ShowTime;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShowTimeSpecification {

    // kiem tra dieu kien co movie name vn hay khong
    public static Specification<ShowTime> hasMovieTitle(String title) {
        // bieu thuc lamda
        return (root, query, builder) -> builder.like(builder.lower(root.get("movie").get("nameVn")), "%" + title.toLowerCase() + "%");
    }

    // kiem tra dieu kien co thuoc ngay do hay khong
    public static Specification<ShowTime> hasDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return (root, query, builder) -> builder.between(root.get("startTime"), startOfDay, endOfDay);
    }

    // kiem tra dieu kien co thuoc thang do hay khong
    public static Specification<ShowTime> hasMonth(LocalDate date) {
        return (root, query, cb) -> cb.and(
                cb.equal(cb.function("MONTH", Integer.class, root.get("startTime")), date.getMonthValue()),
                cb.equal(cb.function("YEAR", Integer.class, root.get("startTime")), date.getYear())
        );
    }

    // kiem tra dieu kien co thuoc nam do hay khong
    public static Specification<ShowTime> hasYear(int year) {
        return (root, query, cb) -> cb.equal(cb.function("YEAR", Integer.class, root.get("startTime")), year);
    }
}
