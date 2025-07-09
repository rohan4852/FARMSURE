package com.farmsure.service;

import com.farmsure.model.Contract;
import com.farmsure.model.User;
import com.farmsure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Implement the required method from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // Other existing methods...

    public List<User> getFarmersByContracts(List<Contract> contracts) {
        List<User> farmers = new ArrayList<>();
        for (Contract contract : contracts) {
            User farmer = contract.getAssignedFarmer();
            if (farmer != null && !farmers.contains(farmer)) {
                farmers.add(farmer);
            }
        }
        return farmers;
    }

    public java.util.Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public java.util.Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        // Add registration logic here if needed (e.g., password encoding)
        return save(user);
    }
}
