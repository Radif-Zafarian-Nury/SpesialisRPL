package com.example.spesialisRPL.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spesialisRPL.Dokter.DokterCard;

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
    public List<PasienData> findAllPendaftaran() {
        String sql = "SELECT * FROM lihat_pendaftaran_pasien";
        return jdbcTemplate.query(sql, this::mapRowToListPasien);
    }

    //AMBIL NAMA DOKTER BERDASARKAN NAMA PASIEN
    @Override
    public List<String> findDoctorNameByPatientName(String nama) {
       String sql = """
               SELECT DISTINCT nama_dokter
               FROM lihat_pendaftaran_pasien
               WHERE nama = ?
               """;
       return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("nama_dokter"), nama);
    }

    @Override
    public List<String> findDoctorsByDay(String tanggal) {
       String sql = """
               SELECT DISTINCT nama
               FROM lihat_jadwal_dokter
               WHERE tanggal = ?
               """;
       return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("nama"), java.sql.Date.valueOf(tanggal));
    }

    @Override
    public List<String> findSpecializationsByDoctor(String dokter) {
        String sql = """
                SELECT DISTINCT nama_spesialisasi
                FROM lihat_jadwal_dokter
                WHERE nama = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("nama_spesialisasi"), dokter);
    }

    @Override
    public List<JadwalDokterData> findSchedulesBySpecialization(String spesialisasi) {
        String sql = """
                SELECT id_jadwal AS idJadwal, nama, nama_spesialisasi, tanggal, waktu_mulai, waktu_selesai
                FROM lihat_jadwal_dokter
                WHERE nama_spesialisasi = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            JadwalDokterData jadwal = new JadwalDokterData(
                resultSet.getInt("idJadwal"),
                resultSet.getString("nama"),
                resultSet.getString("nama_spesialisasi"),
                resultSet.getString("tanggal"),
                resultSet.getString("waktu_mulai"),
                resultSet.getString("waktu_selesai")
            );
            return jadwal;
        }, spesialisasi);
    }

    @Override
    public JadwalDokterData findScheduleById(int idJadwal){
        String sql = "SELECT id_jadwal as idJadwal, kuota_max, kuota_terisi FROM jadwal WHERE id_jadwal = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            return new JadwalDokterData(
                resultSet.getInt("idJadwal"),
                resultSet.getInt("kuota_max"),
                resultSet.getInt("kuota_terisi")
            );
        }, idJadwal);
    }

    @Override
    public Optional<FormPendaftaranData> findNik(String nik) {
        String sql = """
            SELECT
                id_user,
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

    public void incrementKuotaTerisi(int idJadwal){
        String sql = "UPDATE jadwal SET kuota_terisi = kuota_terisi + 1 WHERE id_jadwal = ?";
        jdbcTemplate.update(sql, idJadwal);
    }

    @Override
    public void registerPasien(String nik, int idJadwal){
        String sqlUser = "SELECT id_user FROM users WHERE nik = ?";
        Integer idUser = jdbcTemplate.queryForObject(sqlUser, Integer.class, nik);
        if (idUser == null) {
            throw new IllegalArgumentException("Pasien tidak ditemukan");
        }

        // Tambahkan pasien ke tabel pendaftaran
        String sql = """
            INSERT INTO pendaftaran (id_pasien, id_jadwal, status_daftar_ulang, status_bayar, no_antrian)
            VALUES (?, ?, FALSE, FALSE, (
                SELECT COALESCE(MAX(no_antrian), 0) + 1
                FROM pendaftaran
                WHERE id_jadwal = ?
            ))
        """;
        jdbcTemplate.update(sql, idUser, idJadwal, idJadwal);
    }

    public JadwalDokterData mapRowToJadwalDokter(ResultSet resultSet, int rowNum) throws SQLException {
        return new JadwalDokterData(
            resultSet.getString("nama"),
            resultSet.getString("nama_spesialisasi"),
            resultSet.getString("tanggal"),
            resultSet.getString("waktu_mulai"),
            resultSet.getString("waktu_selesai")
            );
    }

    public PasienData mapRowToListPasien(ResultSet resultSet, int rowNum) throws SQLException {
        return new PasienData(
            resultSet.getString("nama"),
            resultSet.getString("nama_dokter"),
            resultSet.getString("waktu_mulai"),
            resultSet.getString("waktu_selesai"),
            resultSet.getString("tanggal"),
            resultSet.getBoolean("status_bayar"),
            resultSet.getBoolean("status_daftar_ulang"),
            resultSet.getInt("no_antrian")
            );
    }

    @Override
    public List<DokterCard> getAllDoctorCards() {
        String sql = """
            SELECT id_user, nama, foto_dokter, nama_spesialisasi
            FROM dokter_cards
            """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> mapRowToDokterCard(resultSet, rowNum));
    }
        
    private DokterCard mapRowToDokterCard(ResultSet resultSet, int rowNum) throws SQLException {
        return new DokterCard(
            resultSet.getInt("id_user"),
            resultSet.getString("nama"), 
            resultSet.getString("foto_dokter"), 
            resultSet.getString("nama_spesialisasi")
        );
    }
}
