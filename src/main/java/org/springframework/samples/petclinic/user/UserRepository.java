package org.springframework.samples.petclinic.user;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<UserEntity, Serializable> {
    public abstract UserEntity findByUsername(String username);
}
