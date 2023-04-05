package ru.itis.javalab.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.javalab.models.Ad;

import java.util.List;

public interface AdsRepository extends JpaRepository<Ad, Long> {

    Page<Ad> findAllByStateOrderById(Pageable pageable, Ad.State state);
}
