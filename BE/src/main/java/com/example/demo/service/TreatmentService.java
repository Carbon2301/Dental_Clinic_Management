package com.example.demo.service;

import com.example.demo.dto.PatientDto;
import com.example.demo.dto.TreatmentDto;
import com.example.demo.model.Treatment;

import java.util.List;

public interface TreatmentService {
    List<TreatmentDto> findTreatmentByDentistId(int dentistId);
    TreatmentDto save(TreatmentDto treatmentDto );
}
