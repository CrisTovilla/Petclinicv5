package org.springframework.samples.petclinic.user;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<UserEntity, Serializable> {
    public abstract UserEntity findByUsername(String username);
    
    /**
    * Retrieve {@link User}s from the data store by last name, returning all users
    * whose last name <i>starts</i> with the given name.
    * @param lastName Value to search for
    * @return a Collection of matching {@link User} (or an empty Collection if none
    * found)
    */
    @Query("SELECT  user FROM UserEntity user where user.lastName LIKE :lastName% ")
    @Transactional(readOnly = true)
    Collection<UserEntity> findByLastName(@Param("lastName") String lastName);
    
    @Query("SELECT user from UserEntity user WHERE user.username= :userName ")
    @Transactional(readOnly = true)
    UserEntity existUserName(@Param("userName") String userName);
    
    UserEntity save(UserEntity user);          
}