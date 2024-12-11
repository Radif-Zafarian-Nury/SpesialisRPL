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
        return jdbcTemplate.query(sql, new Object[] {idPasien}, this::mapRowToRekamMedis);
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
        RekamMedis rekamMedis = new RekamMedis();
        rekamMedis.setId_pasien(resultSet.getInt("id_pasien"));
        rekamMedis.setTanggal(resultSet.getString("tanggal")); // Pastikan tanggal diambil sebagai String
        rekamMedis.setTinggi_badan(resultSet.getDouble("tinggi_badan"));
        rekamMedis.setBerat_badan(resultSet.getDouble("berat_badan"));
        rekamMedis.setSuhu_tubuh(resultSet.getDouble("suhu_tubuh"));
        rekamMedis.setResep_obat(resultSet.getString("resep_obat"));
        rekamMedis.setDiagnosa_dokter(resultSet.getString("diagnosa_dokter"));
        rekamMedis.setNama(resultSet.getString("nama"));
        rekamMedis.setJenis_kelamin(resultSet.getString("jenis_kelamin"));
        rekamMedis.setTanggal_lahir(resultSet.getString("tanggal_lahir"));
        return rekamMedis;
    }
}
