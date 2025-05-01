package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.AddressDTO;
import com.ecotrack.ecotrack.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .stateProvince(address.getStateProvince())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        return Address.builder()
                .id(dto.getId())
                .street(dto.getStreet())
                .city(dto.getCity())
                .stateProvince(dto.getStateProvince())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }

    public void updateAddressFromDto(AddressDTO dto, Address entity) {
        if (dto == null || entity == null)
            return;

        if (dto.getStreet() != null) {
            entity.setStreet(dto.getStreet());
        }
        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if (dto.getStateProvince() != null) {
            entity.setStateProvince(dto.getStateProvince());
        }
        if (dto.getPostalCode() != null) {
            entity.setPostalCode(dto.getPostalCode());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
    }
}
