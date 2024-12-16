package com.example.spesialisRPL.Pasien;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcPasienRepository implements PasienRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Pasien> findPendaftaranByDate(LocalDate tgl) {
        String sql = """
            SELECT * 
            FROM lihat_pendaftaran_pasien
            WHERE tanggal = ?
            ORDER BY status_bayar, waktu_mulai, no_antrian
                """;
        return jdbcTemplate.query(sql, this::mapRowToListPasien, tgl);
    }

    public Pasien mapRowToListPasien(ResultSet resultSet, int rowNum) throws SQLException {
        return new Pasien(
            resultSet.getInt("id_pasien"),
            resultSet.getString("nama_pasien"),
            resultSet.getString("jenis_kelamin"),
            resultSet.getString("tanggal_lahir"),
            resultSet.getInt("no_rekam_medis"),
            resultSet.getInt("no_antrian"),
            resultSet.getString("waktu_mulai"),
            resultSet.getString("waktu_selesai")
            );
    }

    @Override
    public List<Pasien> findPendaftaranByDateAndName(LocalDate tgl, String name) {
        String sql = """
                SELECT * 
                FROM lihat_pendaftaran_pasien
                WHERE tanggal = ?
                AND (nama_pasien ILIKE ?)
                ORDER BY waktu_mulai, no_antrian
                """;
        return jdbcTemplate.query(sql, this::mapRowToListPasien, tgl, ("%"+name+"%"));
    }
}
