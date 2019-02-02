package org.springframework.samples.petclinic.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.logins.LoginsEntity;
import org.springframework.samples.petclinic.logins.LoginsRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service("UserService")
public class UserService implements UserDetailsService {
    @Autowired
    @Qualifier("UserRepository")
    private UserRepository userRepository;
    
    @Autowired
    @Qualifier("LoginsRepository")
    private LoginsRepository loginsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity user = userRepository.findByUsername(username);
            UserDetails response = new User(user.getUsername(), user.getPassword(), 
                                            user.isActive(), user.isActive(), user.isActive(), 
                                            user.isActive(), buildGranted());
          
            if(user.isActive()) {
                LoginsEntity login = new LoginsEntity();
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                login.setUsername(username);
                login.setDate(df.format(new Date()));
                login.setLogin("Exitoso");

                loginsRepository.save(login);
            }
            else {
                LoginsEntity login = new LoginsEntity();
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                login.setUsername(username);
                login.setDate(df.format(new Date()));
                login.setLogin("Usuario Inactivo");
            
                loginsRepository.save(login);
            }
   
            return response;
        }
        catch(Exception error) {
            LoginsEntity login = new LoginsEntity();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            
            login.setUsername(username);
            login.setDate(df.format(new Date()));
            login.setLogin("Fallido");
            
            loginsRepository.save(login);
            return null;
        }
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
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}