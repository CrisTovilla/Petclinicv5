package org.springframework.samples.petclinic.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import org.springframework.samples.petclinic.model.Person;

@Entity
@Table(name="user")
public class UserEntity extends Person implements Serializable {

    @Column(name="username")
    @NotEmpty
    private String username;
    
    @Column(name="password")
    @NotEmpty
    private String password;
    
    @Column(name="city")
    @NotEmpty
    private String city;
    
    @Column(name="postalcode")
    @NotEmpty
    @Digits(fraction = 0, integer = 5)
    private String postalcode;
    
    @Column(name="active",columnDefinition="default 'true'")
    private boolean active;

   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
    
    
    
}
