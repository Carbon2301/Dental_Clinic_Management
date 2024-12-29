package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.dto.PatientDto;
import java.util.List;
public interface PatientService {
    PatientDto save(PatientDto patientDto);
    List<PatientDto> getAll();
    List<PatientDto> findAll();
    String findNameById(int id);
    List<PatientDto> findPatientByDentistId(int dentistId);
    boolean login(String email, String password);
    boolean register(String email, String password, String name);
    PatientDto findPatientByEmail(String email);
    PatientDto findPatientById(int patientId);
    PatientDto update(PatientDto patientDto);
}
