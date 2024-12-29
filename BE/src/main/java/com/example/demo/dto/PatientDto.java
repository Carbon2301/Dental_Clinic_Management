package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private int id;
    private String fullName;
    private Date dob;
    private String phone;
    private String sex;
    private String email;
    private String password;
    private String address;
    private String image;
}
