--DROP
DROP VIEW IF EXISTS lihat_jadwal_dokter;
DROP VIEW IF EXISTS daftar_dokter;
DROP VIEW IF EXISTS dokter_cards;
DROP VIEW IF EXISTS nama_Dokter_di_jadwal;
DROP VIEW IF EXISTS lihat_pendaftaran_pasien;


--VIEW
CREATE VIEW lihat_jadwal_dokter AS
(SELECT
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

CREATE VIEW nama_Dokter_di_jadwal AS(
SELECT
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
(SELECT DISTINCT
	nama,
	nama_dokter,
	waktu_mulai,
	waktu_selesai,
	tanggal,
	status_bayar,
	status_daftar_ulang,
	no_antrian
	
FROM
	users INNER JOIN pendaftaran
	ON users.id_user = pendaftaran.id_pasien
	INNER JOIN jadwal
	ON jadwal.id_jadwal = pendaftaran.id_jadwal INNER JOIN nama_Dokter_di_jadwal
	ON nama_Dokter_di_jadwal.id_dokter = jadwal.id_dokter
	);
	
--SELECT
SELECT * FROM lihat_jadwal_dokter;
SELECT * FROM daftar_dokter;
SELECT * FROM dokter_cards;
SELECT * FROM nama_Dokter_di_jadwal;
SELECT * FROM lihat_pendaftaran_pasien;
