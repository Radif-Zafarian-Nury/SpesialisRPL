package com.example.spesialisRPL.Doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorJdbc implements DoctorRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Iterable<Doctor> getAllDoctors() {
    
    }
}