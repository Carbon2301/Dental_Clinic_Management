package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Dentist;
import com.example.demo.model.Patient;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final ModelMapper modelMapper;

    @Override
    public AppointmentDto save(AppointmentDto AppointmentDto) {
        Appointment appointment = toEntity(AppointmentDto);
        appointment = appointmentRepository.save(appointment);
        return toDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream().map(this::toDto).toList();
    }

    private Appointment toEntity(AppointmentDto appointmentDto) {
        Optional<Patient> patient = patientRepository.findById(appointmentDto.getPatientId());
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setPatient(patient.orElse(null));
        return appointment;
    }

    private AppointmentDto toDto(Appointment appointment) {
        int patientId = appointment.getPatient().getId();
        AppointmentDto appointmentDto = modelMapper.map(appointment, AppointmentDto.class);
        appointmentDto.setPatientId(patientId);
        return appointmentDto;
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments;
    }

    @Override
    public DentistDto findDentistByAppointmentId(int appointmentId) {
        // Find Dentist associated with the Appointment by ID
        Optional<Dentist> dentistOptional = appointmentRepository.findAppointmentWithDentist(appointmentId);
        return dentistOptional.map(d -> new DentistDto(d.getId(), d.getName(), d.getImgUrl(), d.getSpeciality()))
                .orElse(null); // Return null if no Dentist found
    }

    @Override
    public List<AppointmentDto> findAppointmentsWithDentists() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream().map(appointment -> {
            AppointmentDto appointmentDto = toDto(appointment);
            DentistDto dentistDto = findDentistByAppointmentId(appointment.getId());
            appointmentDto.setDentist(dentistDto);  // Set DentistDto in AppointmentDto
            return appointmentDto;
        }).collect(Collectors.toList());
    }

    @Override
    public AppointmentDto findAppointmentById(int appointmentId) {
        return toDto(appointmentRepository.findById(appointmentId));
    }

    @Override
    public List<AppointmentDto> findAppointmentByDentistId(int dentistId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentByDentistId(dentistId);
        return appointments.stream().map(this::toDto).toList();
    }
    @Override
    public List<AppointmentDto> findAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientId(patientId);
        List<AppointmentDto> appointmentDtos = appointments.stream().map(this::toDto).toList();
        for (AppointmentDto appointment : appointmentDtos) {
            DentistDto dentist = findDentistByAppointmentId(appointment.getAppointmentId());
            appointment.setDentist(dentist);
        }
        return appointmentDtos;
    }

    @Override
    public List<String> getAvailableTimeSlots(int dentistId, Date appointmentDate) {

        List<String> allTimeSlots = generateTimeSlots(LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // Fetch all booked appointments for the dentist on the given date
        List<String> bookedTimeSlots = appointmentRepository
                .findByDentistIdAndAppointmentDate(dentistId, appointmentDate)
                .stream()
                .map(appointment -> appointment.getAppointmentTime().toString()) // Trích xuất và chuyển đổi thời gian cuộc hẹn thành chuỗi
                .collect(Collectors.toList());

        // Filter out booked time slots
        List<String> availableTimeSlots = allTimeSlots.stream()
                .filter(slot -> !bookedTimeSlots.contains(slot))
                .collect(Collectors.toList());

        return availableTimeSlots;

    }


    private List<String> generateTimeSlots(LocalTime startTime, LocalTime endTime, int intervalMinutes) {
        List<String> timeSlots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            timeSlots.add(currentTime.toString());
            currentTime = currentTime.plusMinutes(intervalMinutes);
        }

        return timeSlots;
    }

    @Override
    public Boolean isSlotAvailable(int dentistId, Date appointmentDate, Time appointmentTime) {
        // Fetch appointments for the given dentist and date
        List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndAppointmentDate(dentistId, appointmentDate);

        // Check if any appointment matches the given time
        for (Appointment appointment : existingAppointments) {
            if (appointment.getAppointmentTime().equals(appointmentTime)) {
                return false; // Slot is already booked
            }
        }

        return true; // Slot is available
    }
}
