package com.example.spesialisRPL.Pasien;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPasienRepository implements PasienRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Pasien> findAll() {
        String sql = "SELECT * FROM list_pasien";
        return jdbcTemplate.query(sql, this::mapRowToPasien);
    }


    @Override
    public List<Pasien> searchByMedicalRecordNumber(String noRekamMedis) {
        String sql = "SELECT * FROM list_pasien WHERE no_rekam_medis LIKE ?";
        return jdbcTemplate.query(sql, this::mapRowToPasien, "%" + noRekamMedis + "%");
    }

    private Pasien mapRowToPasien(ResultSet rs, int rowNum) throws SQLException {
        Pasien pasien = new Pasien();
        pasien.setNama(rs.getString("nama"));
        pasien.setJenis_kelamin(rs.getString("jenis_kelamin"));
        pasien.setTanggal_lahir(rs.getString("tanggal_lahir"));
        pasien.setNo_rekam_medis(rs.getString("no_rekam_medis"));
        pasien.setNo_antrian(rs.getString("no_antrian"));
        pasien.setWaktu_mulai(rs.getString("waktu_mulai"));
        pasien.setWaktu_selesai(rs.getString("waktu_selesai"));
        pasien.setId_pasien(rs.getInt("id_pasien"));
        return pasien;
    }

    
}
