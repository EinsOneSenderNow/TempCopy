package ru.itis.javalab.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.javalab.models.Cheque;

public interface ChequesRepository extends JpaRepository<Cheque, Long> {

    public Page<Cheque> findAllByStateOrderById(Pageable pageable, Cheque.State state);
}
