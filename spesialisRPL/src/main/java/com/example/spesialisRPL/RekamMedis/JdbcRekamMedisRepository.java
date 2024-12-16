package com.example.spesialisRPL.RekamMedis;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRekamMedisRepository implements RekamMedisRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RekamMedis> findByIdPasien(int idPasien) {
        String sql = "SELECT * FROM list_rekam_medis WHERE id_pasien = ?";
        return jdbcTemplate.query(sql, this::mapRowToRekamMedis);
    }

    public RekamMedis save(RekamMedis rekamMedis) {
        String sql = "INSERT INTO diagnosa (id_pasien, tanggal, tinggi_badan, berat_badan, suhu_tubuh, resep_obat, diagnosa_dokter) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // Jika 'tanggal' bertipe String, konversikan menjadi java.sql.Date
        Date tanggal = Date.valueOf(rekamMedis.getTanggal()); // Misalkan getTanggal() mengembalikan String dalam format "YYYY-MM-DD"
        
        jdbcTemplate.update(sql, rekamMedis.getId_pasien(), tanggal, rekamMedis.getTinggi_badan(),
                            rekamMedis.getBerat_badan(), rekamMedis.getSuhu_tubuh(), rekamMedis.getResep_obat(),
                            rekamMedis.getDiagnosa_dokter());
        return rekamMedis;
    }

    private RekamMedis mapRowToRekamMedis(ResultSet resultSet, int rowNum) throws SQLException {
        return new RekamMedis(
            resultSet.getInt("id_pasien"),
            resultSet.getString("tanggal"), // Pastikan tanggal diambil sebagai String
            resultSet.getDouble("tinggi_badan"),
            resultSet.getDouble("berat_badan"),
            resultSet.getDouble("suhu_tubuh"),
            resultSet.getString("resep_obat"),
            resultSet.getString("diagnosa_dokter"),
            resultSet.getString("nama"),
            resultSet.getString("jenis_kelamin"),
            resultSet.getString("tanggal_lahir"),
            resultSet.getString("tekanan_darah"),
            resultSet.getString("catatan_tambahan"),
            resultSet.getString("keluhan")
        );
    }
}
