package org.springframework.samples.petclinic.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Service("UserService")
public class UserService implements UserDetailsService {
    @Autowired
    @Qualifier("UserRepository")
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByUsername(username);    
        return new User(user.getUsername(), user.getPassword(), 
                        user.isActive(), user.isActive(), user.isActive(), user.isActive(), buildGranted());
    }
    
    public List<GrantedAuthority> buildGranted() {
        List<GrantedAuthority> auths = new ArrayList<>();
        return auths;
    }
    
    public boolean existPostalCode(String postalCode,String city) throws IOException{
        PostalCodeRest code=new PostalCodeRest();
        return  code.consult(postalCode,city);
    }
    
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
