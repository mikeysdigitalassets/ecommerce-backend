package com.example.ecommercebackend.services;

import com.example.ecommercebackend.dto.ShippingAddressDTO;
import com.example.ecommercebackend.models.ShippingAddress;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repositories.AuthRepository;
import com.example.ecommercebackend.repositories.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    public void saveShippingAddress(ShippingAddressDTO shippingAddressDTO) {
        // find user by user id
        User user = authRepository.findById(shippingAddressDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // creates a new shipment address entity
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setUser(user);
        shippingAddress.setAddress(shippingAddressDTO.getAddress());
        shippingAddress.setCity(shippingAddressDTO.getCity());
        shippingAddress.setState(shippingAddressDTO.getState());
        shippingAddress.setPostalCode(shippingAddressDTO.getPostalCode());
        shippingAddress.setFirstName(shippingAddressDTO.getFirstName());
        shippingAddress.setLastName(shippingAddressDTO.getLastName());

        // save to the repo
        shippingAddressRepository.save(shippingAddress);
    }
}
