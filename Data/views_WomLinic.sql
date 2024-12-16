--DROP
DROP VIEW IF EXISTS lihat_pendaftaran_pasien;
DROP VIEW IF EXISTS lihat_jadwal_dokter;
DROP VIEW IF EXISTS daftar_dokter;
DROP VIEW IF EXISTS dokter_cards;
DROP VIEW IF EXISTS lihat_pendaftaran_pasien;
DROP VIEW IF EXISTS jadwal_dokter_admin_homepage;
DROP VIEW IF EXISTS dokter_info;
DROP VIEW IF EXISTS list_pasien;
DROP VIEW IF EXISTS list_rekam_medis;
DROP VIEW IF EXISTS nama_dokter_di_jadwal;

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
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi
);

CREATE VIEW jadwal_dokter_admin_homepage AS
(SELECT
	jadwal.id_dokter,
	nama,
    STRING_AGG(spesialisasi.nama_spesialisasi, ', ') AS nama_spesialisasi,
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
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi
GROUP BY
	jadwal.id_dokter,
	nama,
	id_jadwal,
	tanggal,
	waktu_mulai,
	waktu_selesai,
	kuota_terisi,
	kuota_max
);


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

CREATE VIEW nama_Dokter_di_jadwal AS(
SELECT
	id_pendaftaran,
	nama AS nama_dokter,
	id_user AS id_dokter,
	id_jadwal
From
 (Select
	id_pendaftaran,
	id_pasien,
	pendaftaran.id_jadwal,
	id_dokter
From
	pendaftaran INNER JOIN jadwal
	ON pendaftaran.id_jadwal = jadwal.id_jadwal) AS Ids INNER JOIN users
	ON Ids.id_dokter = users.id_user
);

CREATE VIEW lihat_pendaftaran_pasien AS
(SELECT
	pendaftaran.id_pendaftaran,
	id_pasien,
	users.nama as nama_pasien, --nama pasien
	jenis_kelamin,
	tanggal_lahir,
	no_rekam_medis,
	nama_dokter,
	nama_spesialisasi,
	waktu_mulai,
	waktu_selesai,
	tanggal,
	status_bayar,
	status_daftar_ulang,
	no_antrian
FROM
	pendaftaran 
INNER JOIN users ON pendaftaran.id_pasien = users.id_user --nama pasien
INNER JOIN nama_Dokter_di_jadwal ON pendaftaran.id_pendaftaran = nama_Dokter_di_jadwal.id_pendaftaran --nama dokter
INNER JOIN spesialisasi ON pendaftaran.id_spesialisasi = spesialisasi.id_spesialisasi --nama spesialisasi
INNER JOIN jadwal ON pendaftaran.id_jadwal = jadwal.id_jadwal --waktu & tanggal
	);

CREATE VIEW dokter_info AS
(SELECT
    id_user,
    nama,
    nik,
	sip,
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
	pendaftaran.id_pasien,
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

CREATE VIEW list_rekam_medis AS
(SELECT 
	id_diagnosa,
	id_pasien,
	nama,
	tanggal_lahir,
	jenis_kelamin,
	tanggal,
	tinggi_badan,
	berat_badan,
	suhu_tubuh,
	tekanan_darah,
	keluhan,
	catatan_tambahan,
	resep_obat,
	diagnosa_dokter
FROM
 	diagnosa INNER JOIN users ON diagnosa.id_pasien = users.id_user);


CREATE VIEW ambil_last_rekam_medis
AS(
	select no_rekam_medis
	from users
	where peran = 'pasien'
	order by no_rekam_medis DESC
	LIMIT 1
);

--SELECT
SELECT * FROM lihat_jadwal_dokter;
SELECT * FROM daftar_dokter;
SELECT * FROM dokter_cards;
SELECT * FROM nama_dokter_di_jadwal;
SELECT * FROM lihat_pendaftaran_pasien;
SELECT * FROM dokter_info;
SELECT * FROM list_pasien;
SELECT * FROM list_rekam_medis;
SELECT * FROM ambil_last_rekam_medis;