package com.example.spesialisRPL.Doctor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spesialisRPL.Admin.JadwalDokterData;

@Repository
public class DoctorJdbc implements DoctorRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Doctor mapRowToDoctor(ResultSet resultSet, int rowNum) throws SQLException {
        byte[] fotoBytes = resultSet.getBytes("foto_dokter");
        String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
        return new Doctor(
            resultSet.getString("nama"),
            fotoBase64,
            resultSet.getString("nama_spesialisasi")
        );
    }

    private DokterCardSelection mapRowToDokterCardSelection(ResultSet resultSet, int rowNum) throws SQLException {
        byte[] fotoBytes = resultSet.getBytes("foto_dokter");
        String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
        return new DokterCardSelection(
            resultSet.getInt("id_dokter"),
            resultSet.getString("nama"),
            resultSet.getString("nama_spesialisasi"),
            fotoBase64
        );
    }

    @Override
    public List<Doctor> getAllDokterMata() {
        String sql = "SELECT * FROM daftar_dokter WHERE nama_spesialisasi = 'Mata' OR nama_spesialisasi = 'Umum'";
        List<Doctor> doctors = jdbcTemplate.query(sql, this::mapRowToDoctor);
        return doctors;
    }

    @Override
    public List<DokterCardSelection> getDoctorScheduleByDate(LocalDate date) {
        String sql = """
                    SELECT 
                        id_dokter, 
                        users.nama, 
                        nama_spesialisasi, 
                        id_jadwal, waktu_mulai, 
                        waktu_selesai, 
                        kuota_terisi, 
                        kuota_max, 
                        foto_dokter, 
                        tanggal 
                    FROM 
                        lihat_jadwal_dokter INNER JOIN users 
                        ON lihat_jadwal_dokter.id_dokter=users.id_user 
                    WHERE 
                        nama_spesialisasi = 'Mata' AND tanggal = ?
                """;
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql, date);
        Map<Integer, DokterCardSelection> dokterMap = new HashMap<>();
        
        for(Map<String, Object> row : resultSet) {
            Integer id_dokter = ((Number) row.get("id_dokter")).intValue();

            DokterCardSelection dokter = dokterMap.computeIfAbsent(id_dokter, id -> {
                byte[] fotoBytes = (byte[]) row.get("foto_dokter");
                String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
                DokterCardSelection newDokterCard = new DokterCardSelection(
                    id_dokter,
                    (String) row.get("nama"),
                    (String) row.get("nama_spesialisasi"),
                    fotoBase64
                );
                return newDokterCard;
            });

            Jadwal jadwal = new Jadwal(
                (String) row.get("waktu_mulai"),
                (String) row.get("waktu_selesai"),
                (Date) row.get("tanggal")
            );
            dokter.getJadwal().add(jadwal);
        }

        return new ArrayList<>(dokterMap.values());
    }

}