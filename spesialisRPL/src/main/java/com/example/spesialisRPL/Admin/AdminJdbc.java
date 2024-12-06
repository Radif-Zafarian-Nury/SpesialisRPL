package com.example.spesialisRPL.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminJdbc implements AdminRepository{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<JadwalDokterData> findAll() {
        String sql = "SELECT * FROM lihat_jadwal_dokter";
        return jdbcTemplate.query(sql, this::mapRowToJadwalDokter);
    }

    @Override
    public Optional<FormPendaftaranData> findNik(String nik) {
        String sql = """
            SELECT
                nama,
                tanggal_lahir AS tanggalLahir,
                jenis_kelamin AS jenisKelamin
            FROM users 
            WHERE nik = ?
        """;
        List<FormPendaftaranData> list = jdbcTemplate.query(
            sql,
            ps -> ps.setString(1, nik),
            new BeanPropertyRowMapper<>(FormPendaftaranData.class)
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public JadwalDokterData mapRowToJadwalDokter(ResultSet resultSet, int rowNum) throws SQLException {
        return new JadwalDokterData(
            resultSet.getString("nama"),
            resultSet.getString("nama_spesialisasi"),
            resultSet.getString("waktu_mulai"),
            resultSet.getString("waktu_selesai")
        );
    }
}
