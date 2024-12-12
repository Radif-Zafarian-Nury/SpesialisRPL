package com.example.spesialisRPL.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbc implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveUser(UserData userData, Date tanggal) {
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin, peran, tempat_lahir, tanggal_lahir, status_aktif) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
        jdbcTemplate.update(
            sql, 
            userData.getNama(), 
            userData.getNik(), 
            userData.getEmail(), 
            userData.getAlamat(), 
            userData.getKata_sandi(), 
            userData.getJenis_kelamin(),
            "pasien",
            userData.getTempat_lahir(), 
            tanggal,
            true);
    }

    @Override
    public void saveUserDariAdmin(UserData userData, Date tanggal) {
        String peran = "dokter";
        if (userData.getPeran().equals("Admin")){
            peran = "Admin";
        }
        else if(userData.getPeran().equals("Perawat")){
            peran = "Perawat";
        }
        else if(userData.getPeran().equals("Pasien")){
            peran = "Pasien";
        }
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin, peran, tempat_lahir, tanggal_lahir, status_aktif) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
        jdbcTemplate.update(
            sql, 
            userData.getNama(), 
            userData.getNik(), 
            userData.getEmail(), 
            userData.getAlamat(), 
            userData.getKata_sandi(), 
            userData.getJenis_kelamin(),
            peran,
            userData.getTempat_lahir(), 
            tanggal,
            true);
    }

    @Override
    public Optional<UserData> findByNik(String nik){
        String sql = "SELECT * FROM users WHERE nik = ?";
        List<UserData> users = jdbcTemplate.query(
            sql, 
            ps -> ps.setString(1, nik),
            new BeanPropertyRowMapper<>(UserData.class)
            );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<UserData> users = jdbcTemplate.query(
            sql, 
            ps -> ps.setString(1, email),
            new BeanPropertyRowMapper<>(UserData.class)
            );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
