--DROP
DROP VIEW IF EXISTS lihat_jadwal_dokter;
DROP VIEW IF EXISTS daftar_dokter;
DROP VIEW IF EXISTS dokter_cards;
DROP VIEW IF EXISTS dokter_info;
DROP VIEW IF EXISTS list_pasien;

--VIEW
CREATE VIEW lihat_jadwal_dokter AS
(SELECT
	jadwal.id_dokter,
	nama,
	nama_spesialisasi,
	id_jadwal,
	tanggal,
	waktu_mulai,
	waktu_selesai,
	kuota_terisi,
	kuota_max
FROM
	users INNER JOIN jadwal
	ON users.id_user = jadwal.id_dokter
	INNER JOIN spesialisasi_dokter
	ON spesialisasi_dokter.id_dokter = users.id_user
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi);
	

CREATE VIEW daftar_dokter AS
(SELECT
	nama,
	foto_dokter,
	nama_spesialisasi
FROM
	users INNER JOIN spesialisasi_dokter
	ON users.id_user = spesialisasi_dokter.id_dokter
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi);

CREATE VIEW dokter_cards AS
(SELECT
	id_user,
    nama,
    foto_dokter,
    STRING_AGG(spesialisasi.nama_spesialisasi, ', ') AS nama_spesialisasi
FROM
    users INNER JOIN spesialisasi_dokter
	ON users.id_user = spesialisasi_dokter.id_dokter
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi
GROUP BY
    users.id_user, users.nama, users.foto_dokter
);

CREATE VIEW dokter_info AS
(SELECT
    id_user,
    nama,
    nik,
    foto_dokter,
    alamat,
    jenis_kelamin
FROM
    users
WHERE
	peran='dokter'
);

CREATE VIEW list_pasien AS
(SELECT
	jadwal.id_dokter,
	no_antrian, 
	waktu_mulai, 
	waktu_selesai, 
	tanggal, 
	nama, 
	jenis_kelamin, 
	tanggal_lahir, 
	no_rekam_medis
FROM
	pendaftaran INNER JOIN jadwal
	ON pendaftaran.id_jadwal = jadwal.id_jadwal
	INNER JOIN users
	ON pendaftaran.id_pasien = users.id_user
WHERE
	no_antrian IS NOT NULL
);
	
--SELECT
SELECT * FROM lihat_jadwal_dokter;
SELECT * FROM daftar_dokter;
SELECT * FROM dokter_cards;
SELECT * FROM dokter_info;
SELECT * FROM list_pasien;
