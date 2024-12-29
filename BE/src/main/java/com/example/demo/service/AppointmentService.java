package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.dto.TreatmentDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Treatment;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDto save(AppointmentDto appointment);
    List<AppointmentDto> getAll();
    List<Appointment> findAll();
    DentistDto findDentistByAppointmentId(int appointmentId);
    List<AppointmentDto> findAppointmentsWithDentists();
    AppointmentDto findAppointmentById(int appointmentId);
    List<AppointmentDto> findAppointmentByDentistId(int dentistId);
    List<AppointmentDto> findAppointmentsByPatientId(int patientId);
    List<String> getAvailableTimeSlots(int dentistId, Date date);
    Boolean isSlotAvailable(int dentistId, Date date, Time time);
}
