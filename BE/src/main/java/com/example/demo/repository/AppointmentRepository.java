package com.example.demo.repository;

import com.example.demo.dto.DentistDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Dentist;
import com.example.demo.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.FROM;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("""
        SELECT d
        FROM Dentist d
        JOIN Treatment t ON d.id = t.dentist.id
        WHERE t.appointment.id = :appointmentId
    """)
    Optional<Dentist> findAppointmentWithDentist(@Param("appointmentId") int appointmentId);

    @Query("SELECT a FROM Appointment a WHERE a.id = :id")
    Appointment findById(@Param("id") int id);

    @Query("""
        SELECT a
        FROM Appointment a
        JOIN Treatment t ON a.id = t.appointment.id
        WHERE t.dentist.id = :dentistId
    """)
    List<Appointment> findAppointmentByDentistId(@Param("dentistId") int dentistId);

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.patient.id = :patientId
    """)
    List<Appointment> findAppointmentsByPatientId(@Param("patientId") int patientId);

    @Query("""
        SELECT a.appointmentTime
        FROM Appointment a
        JOIN Treatment t ON a.id = t.appointment.id
        WHERE t.dentist.id = :dentistId
        AND a.appointmentDate = :date
    """)
    List<Time> getAvailableTimeSlots(@Param("dentistId") int dentistId, @Param("date") Date date);

    @Query("""
        SELECT a
        FROM Appointment a
        JOIN Treatment t ON a.id = t.appointment.id
        WHERE t.dentist.id = :dentistId
        AND a.appointmentDate = :date
    """)
    List<Appointment> findByDentistIdAndAppointmentDate(@Param("dentistId") int dentistId, @Param("date") Date date);

}

