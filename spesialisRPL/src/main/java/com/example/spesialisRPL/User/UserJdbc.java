package com.example.spesialisRPL.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbc implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveUser(UserData userData) {
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
            sql, 
            userData.getNama(), 
            userData.getNik(), 
            userData.getEmail(), 
            userData.getAlamat(), 
            userData.getKata_sandi(), 
            userData.getJenis_kelamin());
    }

}
