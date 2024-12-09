package com.example.spesialisRPL.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spesialisRPL.Doctor.Doctor;
import com.example.spesialisRPL.Doctor.DoctorJdbc;

@Repository
public class UserJdbc implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DoctorJdbc dokterJdbc;

    @Override
    public void saveUser(UserData userData) {
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin, peran) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
            sql, 
            userData.getNama(), 
            userData.getNik(), 
            userData.getEmail(), 
            userData.getAlamat(), 
            userData.getKata_sandi(), 
            userData.getJenis_kelamin(),
            "pasien");
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
    public List<Doctor> findAllDokterMata() {
        List<Doctor> listDokterMata = this.dokterJdbc.getAllDokterMata();
        return listDokterMata;
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
