-- DROP TABLE
DROP TABLE IF EXISTS diagnosa;
DROP TABLE IF EXISTS spesialisasi_dokter;
DROP TABLE IF EXISTS jadwal_dokter;
DROP TABLE IF EXISTS pendaftaran;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS spesialisasi;
DROP TABLE IF EXISTS jadwal;

-- CREATE TABLE
CREATE TABLE users (
	id_user SERIAL PRIMARY KEY,
	nama VARCHAR(100),
	jenis_kelamin CHAR(1),
	kata_sandi VARCHAR(20),
	email VARCHAR(100),
	alamat VARCHAR(200),
	nik VARCHAR(16),
	tempat_lahir VARCHAR(100),
	tanggal_lahir DATE,
	status_aktif BOOLEAN,
	peran VARCHAR(10),
	sip VARCHAR(30),
	no_rekam_medis VARCHAR(10),
	UNIQUE(email, nik, sip, no_rekam_medis)
);
CREATE TABLE spesialisasi(
	id_spesialisasi SERIAL PRIMARY KEY,
	nama_spesialisasi VARCHAR(50)
);
CREATE TABLE jadwal(
	id_jadwal SERIAL PRIMARY KEY,
	hari VARCHAR(10),
	waktu_mulai VARCHAR(5),
	waktu_selesai VARCHAR(5)
);
CREATE TABLE diagnosa(
	id_diagnosa SERIAL,
	id_pasien INT REFERENCES users(id_user),
	tinggi_badan FLOAT,
	berat_badan FLOAT,
	suhu_tubuh FLOAT,
	resep_obat VARCHAR(5000),
	diagnosa_dokter VARCHAR(5000),
	PRIMARY KEY (id_diagnosa, id_pasien)
);
CREATE TABLE spesialisasi_dokter(
	id_dokter INT REFERENCES users(id_user),
	id_spesialisasi INT REFERENCES spesialisasi(id_spesialisasi),
	PRIMARY KEY(id_dokter, id_spesialisasi)	
);
CREATE TABLE jadwal_dokter(
	id_dokter INT REFERENCES users(id_user),
	id_jadwal INT REFERENCES jadwal(id_jadwal),
	PRIMARY KEY (id_dokter, id_jadwal)
);
CREATE TABLE pendaftaran(
	id_dokter INT REFERENCES users(id_user),
	id_pasien INT REFERENCES users(id_user),
	id_jadwal INT REFERENCES jadwal(id_jadwal),
	status_daftar_ulang BOOLEAN,
	status_bayar BOOLEAN,
	PRIMARY KEY (id_dokter, id_pasien, id_jadwal)
);

-- INSERT DATA
INSERT INTO users (nama, jenis_kelamin, kata_sandi, email, alamat, nik, tempat_lahir, tanggal_lahir, status_aktif, peran, sip, no_rekam_medis)
VALUES 
	('Dr. John Doe', 'L', 'johnpwd', 'johndoe@example.com', '123 Elm St, Springfield', '1234567890123456', 'Springfield', '1975-05-20', TRUE, 'dokter', 'SIP123456', NULL),
	('Dr. Jane Smith', 'P', 'janepwd', 'janesmith@example.com', '456 Oak St, Shelbyville', '6543210987654321', 'Shelbyville', '1980-11-15', TRUE, 'dokter', 'SIP654321', NULL),
	('Alice Johnson', 'P', 'alicepwd', 'alicej@example.com', '789 Pine St, Capital City', '1122334455667788', 'Capital City', '1990-07-30', TRUE, 'pasien', NULL, 'RM1001'),
	('Bob Brown', 'L', 'bobpwd', 'bobb@example.com', '101 Maple St, Ogdenville', '2233445566778899', 'Ogdenville', '1985-03-10', TRUE, 'pasien', NULL, 'RM1002'),
	('Michael Johnson', 'L', 'michaelpwd', 'michaelj@example.com', '102 Birch Lane, Denver', '3344556677889900', 'Denver', '1980-02-14', TRUE, 'perawat', 'SIP789123', NULL),
	('Emily Davis', 'P', 'emilypwd', 'emilyd@example.com', '403 Maple Ave, Seattle', '4455667788990011', 'Seattle', '1985-04-18', TRUE, 'perawat', 'SIP456789', NULL),
	('Sarah Thompson', 'P', 'sarahpwd', 'saraht@example.com', '505 Cedar Street, Boston', '5566778899001122', 'Boston', '1993-06-01', TRUE, 'admin', NULL, NULL),
	('James Wilson', 'L', 'jamespwd', 'jamesw@example.com', '606 Elm Drive, Chicago', '6677889900112233', 'Chicago', '1988-08-30', TRUE, 'admin', NULL, NULL);
INSERT INTO spesialisasi (nama_spesialisasi)
VALUES
	('Umum'),
  	('THT'),
  	('Jantung'),
	('Gigi'),
	('Mata');
INSERT INTO jadwal (hari, waktu_mulai, waktu_selesai)
VALUES
	('Senin', '07:00', '08:00'),
	('Senin', '08:00', '09:00'),
	('Senin', '09:00', '10:00'),
	('Senin', '10:00', '11:00'),
	('Senin', '11:00', '12:00'),
	('Senin', '12:00', '13:00'),
	('Senin', '13:00', '14:00'),
	('Senin', '14:00', '15:00'),
	('Senin', '15:00', '16:00'),
	('Senin', '16:00', '17:00'),
	('Senin', '17:00', '18:00'),
	('Selasa', '07:00', '08:00'),
	('Selasa', '08:00', '09:00'),
	('Selasa', '09:00', '10:00'),
	('Selasa', '10:00', '11:00'),
	('Selasa', '11:00', '12:00'),
	('Selasa', '12:00', '13:00'),
	('Selasa', '13:00', '14:00'),
	('Selasa', '14:00', '15:00'),
	('Selasa', '15:00', '16:00'),
	('Selasa', '16:00', '17:00'),
	('Selasa', '17:00', '18:00'),
	('Rabu', '07:00', '08:00'),
	('Rabu', '08:00', '09:00'),
	('Rabu', '09:00', '10:00'),
	('Rabu', '10:00', '11:00'),
	('Rabu', '11:00', '12:00'),
	('Rabu', '12:00', '13:00'),
	('Rabu', '13:00', '14:00'),
	('Rabu', '14:00', '15:00'),
	('Rabu', '15:00', '16:00'),
	('Rabu', '16:00', '17:00'),
	('Rabu', '17:00', '18:00'),
	('Kamis', '07:00', '08:00'),
	('Kamis', '08:00', '09:00'),
	('Kamis', '09:00', '10:00'),
	('Kamis', '10:00', '11:00'),
	('Kamis', '11:00', '12:00'),
	('Kamis', '12:00', '13:00'),
	('Kamis', '13:00', '14:00'),
	('Kamis', '14:00', '15:00'),
	('Kamis', '15:00', '16:00'),
	('Kamis', '16:00', '17:00'),
	('Kamis', '17:00', '18:00'),
	('Jumat', '07:00', '08:00'),
	('Jumat', '08:00', '09:00'),
	('Jumat', '09:00', '10:00'),
	('Jumat', '10:00', '11:00'),
	('Jumat', '11:00', '12:00'),
	('Jumat', '12:00', '13:00'),
	('Jumat', '13:00', '14:00'),
	('Jumat', '14:00', '15:00'),
	('Jumat', '15:00', '16:00'),
	('Jumat', '16:00', '17:00'),
	('Jumat', '17:00', '18:00'),
	('Sabtu', '07:00', '08:00'),
	('Sabtu', '08:00', '09:00'),
	('Sabtu', '09:00', '10:00'),
	('Sabtu', '10:00', '11:00'),
	('Sabtu', '11:00', '12:00'),
	('Sabtu', '12:00', '13:00'),
	('Sabtu', '13:00', '14:00'),
	('Sabtu', '14:00', '15:00'),
	('Sabtu', '15:00', '16:00'),
	('Sabtu', '16:00', '17:00'),
	('Sabtu', '17:00', '18:00'),
	('Minggu', '07:00', '08:00'),
	('Minggu', '08:00', '09:00'),
	('Minggu', '09:00', '10:00'),
	('Minggu', '10:00', '11:00'),
	('Minggu', '11:00', '12:00'),
	('Minggu', '12:00', '13:00'),
	('Minggu', '13:00', '14:00'),
	('Minggu', '14:00', '15:00'),
	('Minggu', '15:00', '16:00'),
	('Minggu', '16:00', '17:00'),
	('Minggu', '17:00', '18:00');
INSERT INTO diagnosa (id_pasien, tinggi_badan, berat_badan, suhu_tubuh, resep_obat, diagnosa_dokter)
VALUES
	(3, 165, 60, 36.7, 'Paracetamol 500mg 3x sehari', 'Demam ringan, disarankan istirahat dan minum cairan.'),
	(4, 180, 75, 37.2, 'Tetes mata 3x sehari, Obat pereda nyeri 200mg 2x sehari', 'Keluhan mata merah dan iritasi, didiagnosa konjungtivitis, disarankan untuk menggunakan tetes mata dan obat pereda nyeri serta istirahatkan mata.');
INSERT INTO spesialisasi_dokter (id_dokter, id_spesialisasi)
VALUES
	(1, 1), 
	(1, 2), 
	(2, 3),
	(2, 4), 
	(2, 5);
INSERT INTO jadwal_dokter (id_dokter, id_jadwal)
VALUES
	(1, 1), 
	(1, 10),
	(2, 20), 
	(2, 30);
INSERT INTO pendaftaran (id_dokter, id_pasien, id_jadwal, status_daftar_ulang, status_bayar)
VALUES
	(1, 3, 10, TRUE, TRUE),
	(2, 4, 20, FALSE, FALSE);

-- SELECT
SELECT * FROM users;
SELECT * FROM spesialisasi;
SELECT * FROM jadwal;
SELECT * FROM diagnosa;
SELECT * FROM spesialisasi_dokter;
SELECT * FROM jadwal_dokter;
SELECT * FROM pendaftaran;
