package com.example.demo.service;

import com.example.demo.config.UserAlreadyExistsException;
import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Dentist;
import com.example.demo.model.Role;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DentistRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {

    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final ModelMapper modelMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @Override
    public DentistDto save(DentistDto registrationDto) {
        if (dentistRepository.findByEmail(registrationDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("Dentist with this email already exists.");
        }
        Role userRole = roleRepository.findByName("ROLE_DENTIST");
        if (userRole == null) {
            userRole = new Role("ROLE_DENTIST");
            userRole = roleRepository.save(userRole);
        }
//        String password = passwordEncoder.encode(registrationDto.getPassword());
//        registrationDto.setPassword(password);
        Dentist user = new Dentist(registrationDto.getImgUrl(),
                registrationDto.getName(),
                registrationDto.getEmail(),
                registrationDto.getPassword(),
                registrationDto.getFees(),
                registrationDto.getSpeciality(),
                registrationDto.getPosition(),
                registrationDto.getStatus(),
                registrationDto.getIsWorking(),
                registrationDto.getAbout(),
                Arrays.asList(userRole));
        Dentist savedDentist = dentistRepository.save(user);
        return modelMapper.map(savedDentist, DentistDto.class);
    }

    @Override
    public boolean login(String email, String password) {
        Dentist dentist = dentistRepository.findByEmail(email);
        if (dentist != null && dentist.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRole(String email) {
        List<Integer> roleIds = dentistRepository.findRoleIdsByEmail(email);
        if(roleIds.contains(2)){
            return true;
        }
        return false;
    }

    @Override
    public List<Dentist> findAll() {
        List<Dentist> dentists = dentistRepository.findAll();
        return dentists;
    }

    @Override
    public DentistDto findDentistById(int dentistId) {
        Optional<Dentist> dentistOptional = dentistRepository.findById(dentistId);
        return dentistOptional.map(d -> modelMapper.map(d, DentistDto.class)).orElse(null);
    }

    @Override
    public DentistDto update(DentistDto dentistDto) {
        Optional<Dentist> dentistOptional = dentistRepository.findById(dentistDto.getDentistId());
        if (dentistOptional.isPresent()) {
            Dentist dentist = dentistOptional.get();
            dentist.setName(dentistDto.getName());
            dentist.setImgUrl(dentistDto.getImgUrl());
            dentist.setFees(dentistDto.getFees());
            dentist.setIsWorking(dentistDto.getIsWorking());
            dentist.setSpeciality(dentistDto.getSpeciality());
            dentist.setStatus(dentistDto.getStatus());
            dentist.setAbout(dentistDto.getAbout());
            dentistRepository.save(dentist);
            return modelMapper.map(dentist, DentistDto.class);
        }
        return null;
    }

    @Override
    public DentistDto findDentistByEmail(String email) {
        Dentist dentist = dentistRepository.findByEmail(email);
        return modelMapper.map(dentist, DentistDto.class);
    }
}
