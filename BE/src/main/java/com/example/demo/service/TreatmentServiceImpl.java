package com.example.demo.service;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.dto.TreatmentDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.Treatment;
import com.example.demo.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final ModelMapper modelMapper;

    private final TreatmentRepository treatmentRepository;

    @Override
    public TreatmentDto save(TreatmentDto treatmentDto) {
        Treatment treatment = toEntity(treatmentDto);
        treatment = treatmentRepository.save(treatment);
        return toDto(treatment);
    }

    private Treatment toEntity(TreatmentDto treatmentDto) {
        Treatment treatment = modelMapper.map(treatmentDto, Treatment.class);
        return treatment;
    }

    private TreatmentDto toDto(Treatment treatment) {
        int patientId = treatment.getPatient().getId();
        int dentistId = treatment.getDentist().getId();
        TreatmentDto treatmentDto = modelMapper.map(treatment, TreatmentDto.class);
        treatmentDto.setPatientId(patientId);
        treatmentDto.setDentistId(patientId);
        return treatmentDto;
    }

    @Override
    public List<TreatmentDto> findTreatmentByDentistId(int dentistId) {
        List<Treatment> treatments = treatmentRepository.findTreatmentByDentistId(dentistId);
        return treatments.stream().map(this::toDto).toList();
    }
}
