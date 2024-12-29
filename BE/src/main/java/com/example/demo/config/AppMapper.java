package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppMapper {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new AppointmentDtoToAppointmentMap());
        modelMapper.addMappings(new TreatmentDtoToTreatmentMap());
        return modelMapper;
    }
}
