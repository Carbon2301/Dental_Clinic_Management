package com.example.demo.config;


import com.example.demo.dto.AppointmentDto;
import com.example.demo.model.Appointment;
import org.modelmapper.PropertyMap;

public class AppointmentDtoToAppointmentMap extends PropertyMap<AppointmentDto, Appointment> {
    @Override
    protected void configure() {
        map().setId(source.getAppointmentId());// Chỉ định ánh xạ từ appointmentId
    }
}
