package com.example.spesialisRPL.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
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
        String sql = "SELECT * FROM jadwal_dokter_admin_homepage";
        return jdbcTemplate.query(sql, this::mapRowToJadwalDokter);
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
    public List<String> findSpecializationsByDoctorID(int id) {
        String sql = """
            SELECT DISTINCT nama_spesialisasi
            FROM lihat_jadwal_dokter
            WHERE id_dokter = ?
            """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("nama_spesialisasi"), id);
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

    @Override
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


    @Override
    public List<DokterCard> getAllDoctorCards() {
        String sql = """
            SELECT id_user, nama, foto_dokter, nama_spesialisasi
            FROM dokter_cards
            """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> mapRowToDokterCard(resultSet, rowNum));
    }
        
    private DokterCard mapRowToDokterCard(ResultSet resultSet, int rowNum) throws SQLException {
        byte[] fotoBytes = resultSet.getBytes("foto_dokter");
        
        // Konversi byte array ke string Base64
        String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
        
        return new DokterCard(
            resultSet.getInt("id_user"),
            resultSet.getString("nama"),
            fotoBase64, 
            resultSet.getString("nama_spesialisasi")
        );
    }
    
    @Override
    public Dokter getDokter(int id) {
        String sql = """
            SELECT id_user, nama, nik, sip, foto_dokter, alamat, jenis_kelamin
            FROM dokter_info
            WHERE id_user = ?
            """;
    
        return jdbcTemplate.queryForObject(sql, this::mapRowToDokter, id);
    }

    private Dokter mapRowToDokter(ResultSet resultSet, int rowNum) throws SQLException {
        byte[] fotoBytes = resultSet.getBytes("foto_dokter");
        // Konversi byte array ke string Base64
        String fotoBase64 = fotoBytes != null ? Base64.getEncoder().encodeToString(fotoBytes) : null;
        return new Dokter(
            resultSet.getInt("id_user"),
            resultSet.getString("nama"), 
            resultSet.getString("nik"),
            resultSet.getString("sip"),
            fotoBase64, 
            resultSet.getString("alamat"),
            resultSet.getString("jenis_kelamin").charAt(0) 
        );
    }

    @Override
    public List<String> getAllSpesialisasi() {
        String sql = """
                SELECT nama_spesialisasi
                FROM spesialisasi
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("nama_spesialisasi"));
    }

    @Override
    public void updateDokter(Dokter dokter, List<String> listSpesialisasi) {
        byte[] fotoDokterBytes = Base64.getDecoder().decode(dokter.getFoto());
        String sql = """
            UPDATE users 
            SET nama = ?, nik = ?, sip = ?, foto_dokter = ?, alamat = ?, jenis_kelamin = ? 
            WHERE id_user = ?
        """;
        jdbcTemplate.update(sql, 
            dokter.getNama(),
            dokter.getNik(),
            dokter.getSip(),
            fotoDokterBytes,
            dokter.getAlamat(),
            dokter.getJenis_kelamin(),
            dokter.getId_user()
        );

        // Update spesialisasi dokter
        String deleteSpecializationsSql = "DELETE FROM spesialisasi_dokter WHERE id_dokter = ?";
        jdbcTemplate.update(deleteSpecializationsSql, dokter.getId_user());

        String spesialisasiSql = """
            INSERT INTO spesialisasi_dokter (id_dokter, id_spesialisasi) 
            SELECT ?, id_spesialisasi FROM spesialisasi WHERE nama_spesialisasi = ?
        """;
        for (String spesialisasi : listSpesialisasi) {
            jdbcTemplate.update(spesialisasiSql, dokter.getId_user(), spesialisasi);
        }
    }

    @Override
    public List<JadwalDokterData> findSchedulesByDate(LocalDate tgl) {
        String sql = """
                SELECT id_jadwal AS idJadwal, nama, nama_spesialisasi, tanggal, waktu_mulai, waktu_selesai
                FROM jadwal_dokter_admin_homepage
                WHERE tanggal = ?
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
        }, tgl);
    }

    @Override
    public List<PasienData> findPendaftaranByDate(LocalDate tgl) {
        String sql = """
            SELECT * 
            FROM lihat_pendaftaran_pasien
            WHERE tanggal = ?
            ORDER BY status_bayar, waktu_mulai, no_antrian
                """;
        return jdbcTemplate.query(sql, this::mapRowToListPasien, tgl);
    }

    public PasienData mapRowToListPasien(ResultSet resultSet, int rowNum) throws SQLException {
        return new PasienData(
            resultSet.getInt("id_pendaftaran"),
            resultSet.getString("nama"),
            resultSet.getString("nama_dokter"),
            resultSet.getString("nama_spesialisasi"),
            resultSet.getString("waktu_mulai"),
            resultSet.getString("waktu_selesai"),
            resultSet.getString("tanggal"),
            resultSet.getBoolean("status_bayar"),
            resultSet.getBoolean("status_daftar_ulang"),
            resultSet.getInt("no_antrian")
            );
    }

    @Override
    public List<PasienData> findPendaftaranByDateAndName(LocalDate tgl, String name) {
        String sql = """
                SELECT * 
                FROM lihat_pendaftaran_pasien
                WHERE tanggal = ?
                AND (nama iLIKE ?)
                ORDER BY waktu_mulai, no_antrian
                """;
        return jdbcTemplate.query(sql, this::mapRowToListPasien, tgl,name);
    }
 
    @Override
    public List<PasienData> updatePembayaran(int id){
        String sql = """
                UPDATE pendaftaran
                SET status_bayar = true
                WHERE id_pendaftaran = ?
                """;
        jdbcTemplate.update(sql,id);

        sql = """
            SELECT * 
                FROM lihat_pendaftaran_pasien
                WHERE id_pendaftaran = ?
                ORDER BY waktu_mulai, no_antrian
            """;

        return jdbcTemplate.query(sql, this::mapRowToListPasien, id);
    }
}