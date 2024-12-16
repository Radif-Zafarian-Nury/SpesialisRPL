package com.example.spesialisRPL.RekamMedis;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRekamMedisRepository implements RekamMedisRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RekamMedis> findByIdPasien(int idPasien) {
        String sql = "SELECT * FROM list_rekam_medis WHERE id_pasien = ?";
        List<RekamMedis> rekamMedisList = jdbcTemplate.query(sql, this::mapRowToRekamMedis, idPasien);

        return rekamMedisList;
    }

    public RekamMedis save(RekamMedis rekamMedis) {
        String sql = "INSERT INTO diagnosa (id_pasien, tanggal, tinggi_badan, berat_badan, suhu_tubuh, tekanan_darah, keluhan, informasi_tambahan, resep_obat, diagnosa_dokter) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Jika 'tanggal' bertipe String, konversikan menjadi java.sql.Date
        Date tanggal = Date.valueOf(rekamMedis.getTanggal()); // Misalkan getTanggal() mengembalikan String dalam format "YYYY-MM-DD"
        
        byte[] fotoInformasiTambahanBytes = null;
        if (rekamMedis.getInformasi_tambahan() != null && !rekamMedis.getInformasi_tambahan().isEmpty()) {
            try {
                fotoInformasiTambahanBytes = Base64.getDecoder().decode(rekamMedis.getInformasi_tambahan());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid base64 string for informasi_tambahan", e);
            }
        }
        jdbcTemplate.update(sql, 
                            rekamMedis.getId_pasien(), 
                            tanggal, 
                            rekamMedis.getTinggi_badan(),
                            rekamMedis.getBerat_badan(), 
                            rekamMedis.getSuhu_tubuh(), 
                            rekamMedis.getTekanan_darah(), 
                            rekamMedis.getKeluhan(), 
                            fotoInformasiTambahanBytes, 
                            rekamMedis.getResep_obat(),
                            rekamMedis.getDiagnosa_dokter());
        return rekamMedis;
    }

    private RekamMedis mapRowToRekamMedis(ResultSet resultSet, int rowNum) throws SQLException {
        byte[] fotoBytes = resultSet.getBytes("informasi_tambahan");
        
        // Konversi byte array ke string Base64
        String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
       
        return new RekamMedis(
            resultSet.getInt("id_diagnosa"),
            resultSet.getInt("id_pasien"),
            resultSet.getDate("tanggal").toString(),
            resultSet.getDouble("tinggi_badan"),
            resultSet.getDouble("berat_badan"),
            resultSet.getDouble("suhu_tubuh"),
            resultSet.getString("resep_obat"),
            resultSet.getString("diagnosa_dokter"),
            resultSet.getString("nama"),
            resultSet.getString("jenis_kelamin"),
            resultSet.getDate("tanggal_lahir").toString(),
            resultSet.getString("tekanan_darah"),
            fotoBase64,
            resultSet.getString("keluhan")
        );
    }

    @Override
    public RekamMedis getRekamMedisTerbaru(int idPasien) {
        String sql = "SELECT * FROM list_rekam_medis WHERE id_pasien = ? ORDER BY id_diagnosa DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToRekamMedis, idPasien);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }    
    }

    @Override
    public void updateDariDokter(RekamMedis rekamMedis) {
        String getIdDiagnosaSql = "SELECT id_diagnosa FROM list_rekam_medis WHERE id_pasien = ? ORDER BY id_diagnosa DESC LIMIT 1";

        Integer id_diagnosa = jdbcTemplate.queryForObject(getIdDiagnosaSql, Integer.class, rekamMedis.getId_pasien());
    
        String sql = "UPDATE diagnosa SET diagnosa_dokter = ?, resep_obat = ? WHERE id_diagnosa = ?";
        jdbcTemplate.update(sql, rekamMedis.getDiagnosa_dokter(), rekamMedis.getResep_obat(), id_diagnosa);
    }

    @Override
    public RekamMedis getRekamMedisById(int id) {
        String sql = "SELECT * FROM list_rekam_medis WHERE id_diagnosa = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToRekamMedis, id);
    }
    
}
