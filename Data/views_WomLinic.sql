--DROP
DROP VIEW IF EXISTS lihat_jadwal_dokter;
DROP VIEW IF EXISTS daftar_dokter;
DROP VIEW IF EXISTS dokter_cards;

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

	
--SELECT
SELECT * FROM lihat_jadwal_dokter;
SELECT * FROM daftar_dokter;
SELECT * FROM dokter_cards;