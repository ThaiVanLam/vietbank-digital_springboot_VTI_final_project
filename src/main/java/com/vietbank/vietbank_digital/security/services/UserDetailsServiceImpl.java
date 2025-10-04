package com.vietbank.vietbank_digital.security.services;


import com.vietbank.vietbank_digital.model.User;
import com.vietbank.vietbank_digital.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Kiểm tra trạng thái user
        if (user.getStatus() == User.Status.INACTIVE) {
            throw new UsernameNotFoundException("User account has been deactivated");
        }

        if (user.getStatus() == User.Status.LOCKED) {
            throw new UsernameNotFoundException("User account has been locked");
        }

        return UserDetailsImpl.build(user);
    }
}
