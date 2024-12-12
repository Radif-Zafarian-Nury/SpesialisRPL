package com.example.spesialisRPL.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorJdbc implements DoctorRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Doctor mapRowToDoctor(ResultSet resultSet, int rowNum) throws SQLException {
        return new Doctor(
            resultSet.getString("nama"),
            resultSet.getString("foto_dokter"),
            resultSet.getString("nama_spesialisasi")
        );
    }

    @Override
    public List<Doctor> getAllDokterMata() {
        String sql = "SELECT * FROM daftar_dokter WHERE nama_spesialisasi = 'Mata' OR nama_spesialisasi = 'Umum'";
        List<Doctor> doctors = jdbcTemplate.query(sql, this::mapRowToDoctor);
        return doctors;
    }
}