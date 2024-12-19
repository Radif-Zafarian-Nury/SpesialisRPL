package com.example.spesialisRPL.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spesialisRPL.Doctor.Doctor;
import com.example.spesialisRPL.Doctor.DoctorJdbc;
import com.example.spesialisRPL.Doctor.DokterCardSelection;

@Repository
public class UserJdbc implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DoctorJdbc dokterJdbc;

    @Override
    public void saveUser(UserData userData) {
        String query = "SELECT * FROM ambil_last_rekam_medis";
        Integer rekamMedis = jdbcTemplate.queryForObject(query, Integer.class) + 1;
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin, peran, tempat_lahir, tanggal_lahir, status_aktif, no_rekam_medis) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
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
            userData.getTanggal_lahir(),
            true,
            rekamMedis);
    }

    @Override
    public void saveUserDariAdmin(UserData userData) {
        String sip = null;
        String peran = "dokter";
        Integer rekamMedis = null;
        if (userData.getPeran().equals("Admin")){
            peran = "Admin";
        }
        else if(userData.getPeran().equals("Perawat")){
            peran = "Perawat";
        }
        else if(userData.getPeran().equals("Pasien")){
            peran = "Pasien";
            String query = "SELECT * FROM ambil_last_rekam_medis";
            rekamMedis = jdbcTemplate.queryForObject(query, Integer.class) + 1;
        }
        else{
            sip = userData.getSip();
        }
        String sql = "INSERT INTO users(nama, nik, email, alamat, kata_sandi, jenis_kelamin, peran, tempat_lahir, tanggal_lahir, status_aktif, no_rekam_medis, sip) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
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
            userData.getTanggal_lahir(),
            true,
            rekamMedis,
            sip);
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
    public List<DokterCardSelection> findFilteredDokterMata(LocalDate date) {
        List<DokterCardSelection> listFilteredDokter = this.dokterJdbc.getDoctorMataScheduleByDate(date);
        return listFilteredDokter;
    }

    @Override
    public List<DokterCardSelection> findFilteredDokterGigi(LocalDate date) {
        List<DokterCardSelection> listFilteredDokter = this.dokterJdbc.getDoctorGigiScheduleByDate(date);
        return listFilteredDokter;
    }

    @Override
    public List<DokterCardSelection> findFilteredDokterTht(LocalDate date) {
        List<DokterCardSelection> listFilteredDokter = this.dokterJdbc.getDoctorThtScheduleByDate(date);
        return listFilteredDokter;
    }

    @Override
    public List<DokterCardSelection> findFilteredDokterJantung(LocalDate date) {
        List<DokterCardSelection> listFilteredDokter = this.dokterJdbc.getDoctorJantungScheduleByDate(date);
        return listFilteredDokter;
    }

    @Override
    public Optional<DokterCardSelection> findDokterMataById(int id_dokter, Date tanggal) {
        Optional<DokterCardSelection> dokterMata = this.dokterJdbc.getScheduledDoctorMataById(id_dokter, tanggal);
        return dokterMata;
    }

    @Override
    public Optional<DokterCardSelection> findDokterGigiById(int id_dokter, Date tanggal) {
        Optional<DokterCardSelection> dokterGigi = this.dokterJdbc.getScheduledDoctorGigiById(id_dokter, tanggal);
        return dokterGigi;
    }

    @Override
    public Optional<DokterCardSelection> findDokterThtById(int id_dokter, Date tanggal) {
        Optional<DokterCardSelection> dokterTht = this.dokterJdbc.getScheduledDoctorThtById(id_dokter, tanggal);
        return dokterTht;
    }

    @Override
    public Optional<DokterCardSelection> findDokterJantungById(int id_dokter, Date tanggal) {
        Optional<DokterCardSelection> dokterJantung = this.dokterJdbc.getScheduledDoctorJantungById(id_dokter, tanggal);
        return dokterJantung;
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

    @Override
    public int getPatientIdByNik(String nik) {
        String sql = """
                SELECT 
                    id_user
                FROM
                    users
                WHERE
                    nik = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, nik);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public void daftarPasien(int id_pasien, int id_jadwal, int id_spesialisasi) {
        String sql = """
                INSERT INTO pendaftaran (id_pasien, id_jadwal, id_spesialisasi, status_daftar_ulang, status_bayar, no_antrian)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, id_pasien, id_jadwal, id_spesialisasi, false, false, null);
    }

    @Override
    public boolean cekPasienTerdaftarJadwal(int id_pasien, int id_jadwal) {
        String sql = """
                SELECT COUNT(*)
                FROM pendaftaran
                WHERE id_pasien = ? AND id_jadwal = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id_pasien, id_jadwal);
        return count != null && count > 0;
    }

    @Override
    public int getIdSpesialisasi(String nama_spesialisasi) {
        String sql = "SELECT id_spesialisasi FROM spesialisasi WHERE nama_spesialisasi = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, nama_spesialisasi);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}
