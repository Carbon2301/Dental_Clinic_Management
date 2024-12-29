package com.example.demo.repository;

import com.example.demo.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    @Query("""
        SELECT t
        FROM Treatment t
        JOIN Appointment a ON a.patient.id = t.patient.id
        WHERE t.dentist.id = :dentistId
    """)
    List<Treatment> findTreatmentByDentistId(@Param("dentistId") int dentistId);
}
