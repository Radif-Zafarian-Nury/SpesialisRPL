-- DROP TABLE
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS diagnosa CASCADE;
DROP TABLE IF EXISTS spesialisasi CASCADE;
DROP TABLE IF EXISTS spesialisasi_dokter CASCADE;
DROP TABLE IF EXISTS jadwal CASCADE;
DROP TABLE IF EXISTS pendaftaran CASCADE;

-- CREATE TABLE
CREATE TABLE users (
	id_user SERIAL PRIMARY KEY,
	nama VARCHAR(100),
	jenis_kelamin CHAR(1),
	kata_sandi VARCHAR(60),
	email VARCHAR(100),
	alamat VARCHAR(200),
	nik VARCHAR(16),
	tempat_lahir VARCHAR(100),
	tanggal_lahir DATE,
	status_aktif BOOLEAN,
	peran VARCHAR(10),
	sip VARCHAR(30),
	foto_dokter BYTEA,
	no_rekam_medis INT,
	UNIQUE(email, nik, sip, no_rekam_medis)
);
CREATE TABLE spesialisasi(
	id_spesialisasi SERIAL PRIMARY KEY,
	nama_spesialisasi VARCHAR(50)
);
CREATE TABLE diagnosa(
	id_diagnosa SERIAL,
	id_pasien INT REFERENCES users(id_user),
	tanggal DATE,
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
CREATE TABLE jadwal(
	id_jadwal SERIAL PRIMARY KEY,
	id_dokter INT REFERENCES users(id_user),
	waktu_mulai VARCHAR(5),
	waktu_selesai VARCHAR(5),
	tanggal DATE,
	kuota_max INT,
	kuota_terisi INT
);

CREATE TABLE pendaftaran(
	id_pendaftaran SERIAL, 
	id_pasien INT REFERENCES users(id_user),
	id_jadwal INT REFERENCES jadwal(id_jadwal),
	id_spesialisasi INT REFERENCES spesialisasi(id_spesialisasi),
	status_daftar_ulang BOOLEAN,
	status_bayar BOOLEAN,
	no_antrian INT,
	PRIMARY KEY (id_pendaftaran, id_pasien)
);

-- INSERT DATA
INSERT INTO users (nama, jenis_kelamin, kata_sandi, email, alamat, nik, tempat_lahir, tanggal_lahir, status_aktif, peran, sip, foto_dokter, no_rekam_medis)
VALUES 
	('Dr. John Doe', 'L', '$2a$10$4t1vALs2VUw05yNhqF1snuGQffclS2nZmmcvF6G0kVfQhXpKXJQsS', 'johndoe@example.com', '123 Elm St, Springfield', '1234567890123456', 'Springfield', '1975-05-20', TRUE, 'dokter', 'SIP123456', 'DrJohn.jpg', NULL),
	('Dr. Jane Smith', 'P', '$2a$10$ikWf19/RfrPNZ.kfBxXyPOSbAugTCd3uWcbgJYyz6Jyt3rA5/U3UO', 'janesmith@example.com', '456 Oak St, Shelbyville', '6543210987654321', 'Shelbyville', '1980-11-15', TRUE, 'dokter', 'SIP654321', 'DrJane.jpg', NULL),
	('Alice Johnson', 'P', '$2a$10$P4J0RwnrKzJr9w0UOzUCr.b4YAiOOpShHlPZJb5SpQAYsqbzLOlci', 'alicej@example.com', '789 Pine St, Capital City', '1122334455667788', 'Capital City', '1990-07-30', TRUE, 'pasien', NULL, NULL, 1),
	('Bob Brown', 'L', '$2a$10$WsVrC5Z3VkDXjxOrIyMM3.bmCJKfAWHp1nCPI5xzFa9aYqnmLECdm', 'bobb@example.com', '101 Maple St, Ogdenville', '2233445566778899', 'Ogdenville', '1985-03-10', TRUE, 'pasien', NULL, NULL, 2),
	('Michael Johnson', 'L', '$2a$10$njKUErhgI5TTqT/jEzNeGe2wr1ntZGF81XVj3BZIO1X3G0/TcI9t6', 'michaelj@example.com', '102 Birch Lane, Denver', '3344556677889900', 'Denver', '1980-02-14', TRUE, 'perawat', 'SIP789123',NULL, NULL),
	('Emily Davis', 'P', '$2a$10$we5DEb0/6I4Jm999628l4Ohob9JWbaK47pldl.6xLUTHJdB5deVLu', 'emilyd@example.com', '403 Maple Ave, Seattle', '4455667788990011', 'Seattle', '1985-04-18', TRUE, 'perawat', 'SIP456789', NULL, NULL),
	('Sarah Thompson', 'P', '$2a$10$7vuqZ/HFMuNs2z5JmHkwPeeIxpGiiwOv/ffZ2zYJSKN6NzVbi6gfy', 'saraht@example.com', '505 Cedar Street, Boston', '5566778899001122', 'Boston', '1993-06-01', TRUE, 'admin', NULL, NULL, NULL),
	('James Wilson', 'L', '$2a$10$DksKahS1XjYz5s.aJ1V49ex7JjJuDgRbONRi2N8L0/2lt36ulbFx6', 'jamesw@example.com', '606 Elm Drive, Chicago', '6677889900112233', 'Chicago', '1988-08-30', TRUE, 'admin', NULL, NULL, NULL);
INSERT INTO spesialisasi (nama_spesialisasi)
VALUES
	('Umum'),
  	('THT'),
  	('Jantung'),
	('Gigi'),
	('Mata');
INSERT INTO diagnosa (id_pasien, tanggal, tinggi_badan, berat_badan, suhu_tubuh, resep_obat, diagnosa_dokter)
VALUES
	(3, '2024-12-01', 165, 60, 36.7, 'Paracetamol 500mg 3x sehari', 'Demam ringan, disarankan istirahat dan minum cairan.'),
	(4, '2024-12-02', 180, 75, 37.2, 'Tetes mata 3x sehari, Obat pereda nyeri 200mg 2x sehari', 'Keluhan mata merah dan iritasi, didiagnosa konjungtivitis, disarankan untuk menggunakan tetes mata dan obat pereda nyeri serta istirahatkan mata.');
INSERT INTO spesialisasi_dokter (id_dokter, id_spesialisasi)
VALUES
	(1, 1), 
	(1, 2), 
	(2, 3),
	(2, 4), 
	(2, 5);
INSERT INTO jadwal (id_dokter, waktu_mulai, waktu_selesai, tanggal, kuota_max, kuota_terisi)
VALUES
	(1, '07:00', '10:00', '2024-12-01', 10, 1),
	(2, '07:00', '10:00', '2024-12-02', 10, 1),
	(1, '07:00', '10:00', '2024-12-20', 10, 1), 
	(1, '13:00', '15:00', '2024-12-20', 5, 0),
	(1, '13:00', '15:00', '2024-12-21', 5, 0), 
	(2, '09:00', '10:00', '2024-12-20', 8, 2),  
	(2, '13:00', '15:00', '2024-12-20', 7, 0),  
	(2, '13:00', '15:00', '2024-12-21', 9, 0);
INSERT INTO pendaftaran (id_pasien, id_jadwal, id_spesialisasi, status_daftar_ulang, status_bayar, no_antrian)
VALUES
	(3, 1, 1, TRUE, TRUE, 1), --1
	--Ceritanya belom daftar ulang
	(4, 1, 2, FALSE, FALSE, NULL), --1
	(3, 2, 3, TRUE, TRUE, 1), --2
	(4, 3, 2, TRUE, TRUE, 1), --1
	(3, 6, 4, TRUE, FALSE, 1), --2
	(4, 6, 5, TRUE, FALSE, 2); --2

-- SELECT
SELECT * FROM users;
SELECT * FROM diagnosa;
SELECT * FROM spesialisasi;
SELECT * FROM spesialisasi_dokter;
SELECT * FROM jadwal;
SELECT * FROM pendaftaran;
