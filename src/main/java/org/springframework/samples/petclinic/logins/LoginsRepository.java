package org.springframework.samples.petclinic.logins;

import java.io.Serializable;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("LoginsRepository")
public interface LoginsRepository extends JpaRepository<LoginsEntity, Serializable> {
    @Transactional(readOnly = true)
    @Override
    List<LoginsEntity> findAll() throws DataAccessException;
    
    @Override
    LoginsEntity save(LoginsEntity login);  
}
