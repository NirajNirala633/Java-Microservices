package com.hotel.service.HotelService.services;

import java.util.List;

import com.hotel.service.HotelService.entities.Hotel;

public interface HotelService {
    
    // create
    Hotel create(Hotel hotel);

    // getall
    List<Hotel> getAll();

    // get single
    Hotel get(String id);


}
