package com.example.demo.service;


import com.example.demo.dto.DentistDto;
import com.example.demo.model.Dentist;

import java.util.List;
import java.util.Optional;

public interface DentistService {
    DentistDto save(DentistDto dentist);
    boolean login(String email, String password);
    boolean checkRole(String email);
    List<Dentist> findAll();
    DentistDto findDentistById(int dentist);
    DentistDto update(DentistDto dentist);
    DentistDto findDentistByEmail(String email);
}
