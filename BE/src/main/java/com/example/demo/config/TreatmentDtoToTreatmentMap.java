package com.example.demo.config;

import com.example.demo.dto.TreatmentDto;
import com.example.demo.model.Treatment;
import org.modelmapper.PropertyMap;


public class TreatmentDtoToTreatmentMap extends PropertyMap<TreatmentDto, Treatment> {
    @Override
    protected void configure() {
        map().setId(source.getTreatmentId()); // Chỉ định ánh xạ từ getTreatmentId()
    }
}
