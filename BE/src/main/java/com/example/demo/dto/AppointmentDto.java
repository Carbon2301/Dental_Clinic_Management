package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private int appointmentId;
    private String patientName;
    private int patientId;
    private String patientImgUrl;
    private Date appointmentDate;
    private Time appointmentTime;
    private String notes;
    private Boolean cancelled;
    private Boolean isCompleted;
    private DentistDto dentist;
    private double fees;
    private Boolean isPaid;
}
