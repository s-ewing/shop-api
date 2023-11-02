package com.example.shopapi.mappers;

import com.example.shopapi.dto.AddressDTO;
import com.example.shopapi.models.Address;

public class AddressMapper {
    public static AddressDTO mapEntityToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreetAddress(address.getStreetAddress());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    public static Address mapAddressDTOtoEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        return address;
    }
}
