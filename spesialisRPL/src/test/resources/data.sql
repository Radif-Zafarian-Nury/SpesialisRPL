INSERT INTO users (id_user, nama) VALUES (1, 'John Doe');
INSERT INTO spesialisasi (id_spesialisasi, nama_spesialisasi) VALUES (1, 'Mata');
INSERT INTO nama_Dokter_di_jadwal (id_pendaftaran, nama_dokter) VALUES (1, 'Dr. Smith');
INSERT INTO jadwal (id_jadwal, waktu_mulai, waktu_selesai, tanggal) VALUES (1, '10:00', '11:00', '2024-12-20');
INSERT INTO pendaftaran (id_pendaftaran, id_pasien, id_spesialisasi, id_jadwal, status_bayar, status_daftar_ulang, no_antrian)
VALUES (1, 1, 1, 1, 0, 0, 1);
