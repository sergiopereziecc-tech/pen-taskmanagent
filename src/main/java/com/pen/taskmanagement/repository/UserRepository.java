package com.pen.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pen.taskmanagement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    
    

}
