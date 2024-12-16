CREATE TABLE users (
    id_user BIGINT PRIMARY KEY,
    nama VARCHAR(255)
);

CREATE TABLE spesialisasi (
    id_spesialisasi BIGINT PRIMARY KEY,
    nama_spesialisasi VARCHAR(255)
);

CREATE TABLE nama_Dokter_di_jadwal (
    id_pendaftaran BIGINT PRIMARY KEY,
    nama_dokter VARCHAR(255)
);

CREATE TABLE jadwal (
    id_jadwal BIGINT PRIMARY KEY,
    waktu_mulai VARCHAR(255),
    waktu_selesai VARCHAR(255),
    tanggal DATE
);

CREATE TABLE pendaftaran (
    id_pendaftaran BIGINT PRIMARY KEY,
    id_pasien BIGINT,
    id_spesialisasi BIGINT,
    id_jadwal BIGINT,
    status_bayar INT,
    status_daftar_ulang INT,
    no_antrian INT
);
