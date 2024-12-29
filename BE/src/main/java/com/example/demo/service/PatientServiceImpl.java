package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Dentist;
import com.example.demo.model.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PatientDto save(PatientDto patientDto) {
        Patient patient = toEntity(patientDto);
        patient = patientRepository.save(patient);
        return toDto(patient);
    }

    @Override
    public List<PatientDto> getAll() {
        List<Patient> Patient = patientRepository.findAll();
        return Patient.stream().map(this::toDto).toList();
    }

    private Patient toEntity(PatientDto patientDto) {
        Patient patient = modelMapper.map(patientDto, Patient.class);
        return patient;
    }

    private PatientDto toDto(Patient patient) {
        PatientDto patientDto = modelMapper.map(patient, PatientDto.class);
        return patientDto;
    }

    @Override
    public List<PatientDto> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::toDto).toList();
    }

    @Override
    public String findNameById(int id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return patient.getFullName();
    }

    @Override
    public List<PatientDto> findPatientByDentistId(int dentistId) {
        List<Patient> patients = patientRepository.findPatientByDentistId(dentistId);
        return patients.stream().map(this::toDto).toList();
    }

    @Override
    public boolean login(String email, String password) {
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null && patient.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean register(String email, String password, String name) {
        Patient patient = patientRepository.findByEmail(email);
        if(patient != null){
            return false;
        }
        Patient newPatient = new Patient();
        newPatient.setEmail(email);
        newPatient.setPassword(password);
        newPatient.setFullName(name);
        patientRepository.save(newPatient);
        return true;
    }

    @Override
    public PatientDto findPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email);
        return modelMapper.map(patient, PatientDto.class);
    }

    @Override
    public PatientDto findPatientById(int dentistId) {
        Optional<Patient> patientOptional = patientRepository.findById(dentistId);
        return patientOptional.map(d -> modelMapper.map(d, PatientDto.class)).orElse(null);
    }

    @Override
    public PatientDto update(PatientDto patientDto) {
        Optional<Patient> patientOptional = patientRepository.findById(patientDto.getId());
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patient.setFullName(patientDto.getFullName());
            patient.setImage(patientDto.getImage());
            patient.setEmail(patientDto.getEmail());
            patient.setDob(patientDto.getDob());
            patient.setAddress(patientDto.getAddress());
            patient.setPhone(patientDto.getPhone());
            patientRepository.save(patient);
            return modelMapper.map(patient, PatientDto.class);
        }
        return null;
    }


}
