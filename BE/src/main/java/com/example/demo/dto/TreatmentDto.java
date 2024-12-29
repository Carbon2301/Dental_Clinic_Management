package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentDto {
    // phải tham chieu tu Service va Medicine de them vao trang cho bac si viet don cho benh nhan
    private int treatmentId;
    private int patientId;
    private int dentistId;
    private double fees;
    private String notes;
    private int appointmentId;
}
