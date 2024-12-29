package com.example.demo.repository;

import com.example.demo.model.Dentist;
import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String email);

    @Query("""
        SELECT p
        FROM Patient p
        JOIN Treatment t ON p.id = t.patient.id
        JOIN Dentist d ON d.id = t.dentist.id
        WHERE d.id = :dentistId
    """)
    List<Patient> findPatientByDentistId(int dentistId);
}
