package com.snackshop.service;

import com.snackshop.model.Address;
import com.snackshop.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getUserAddresses(String userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address getAddressById(String id, String userId) {
        return addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("地址不存在"));
    }

    public Address addAddress(String userId, Address address) {
        // 如果是第一个地址，设为默认
        List<Address> existingAddresses = addressRepository.findByUserId(userId);
        if (existingAddresses.isEmpty()) {
            address.setIsDefault(true);
        } else if (address.getIsDefault()) {
            // 如果新地址是默认地址，取消其他默认地址
            existingAddresses.stream()
                    .filter(Address::getIsDefault)
                    .forEach(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
        }

        address.setUserId(userId);
        return addressRepository.save(address);
    }

    public Address updateAddress(String id, String userId, Address updatedAddress) {
        Address existingAddress = getAddressById(id, userId);

        existingAddress.setName(updatedAddress.getName());
        existingAddress.setPhone(updatedAddress.getPhone());
        existingAddress.setProvince(updatedAddress.getProvince());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setDistrict(updatedAddress.getDistrict());
        existingAddress.setDetail(updatedAddress.getDetail());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());

        // 处理默认地址
        if (updatedAddress.getIsDefault() && !existingAddress.getIsDefault()) {
            // 取消其他默认地址
            addressRepository.findByUserId(userId).stream()
                    .filter(Address::getIsDefault)
                    .forEach(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
            existingAddress.setIsDefault(true);
        }

        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(String id, String userId) {
        Address address = getAddressById(id, userId);

        // 如果是默认地址，需要设置其他地址为默认
        if (address.getIsDefault()) {
            List<Address> otherAddresses = addressRepository.findByUserId(userId);
            otherAddresses.removeIf(addr -> addr.getId().equals(id));

            if (!otherAddresses.isEmpty()) {
                Address newDefault = otherAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }

        addressRepository.deleteById(id);
    }

    public Address setDefaultAddress(String id, String userId) {
        // 取消所有默认地址
        addressRepository.findByUserId(userId).stream()
                .filter(Address::getIsDefault)
                .forEach(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });

        // 设置新默认地址
        Address address = getAddressById(id, userId);
        address.setIsDefault(true);
        return addressRepository.save(address);
    }

    public Optional<Address> getDefaultAddress(String userId) {
        List<Address> defaultAddresses = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        return defaultAddresses.isEmpty() ? Optional.empty() : Optional.of(defaultAddresses.get(0));
    }
}