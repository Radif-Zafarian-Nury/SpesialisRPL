--DROP
DROP VIEW IF EXISTS lihat_jadwal_dokter;
DROP VIEW IF EXISTS daftar_dokter;

--VIEW
CREATE VIEW lihat_jadwal_dokter AS
(SELECT
	nama,
	nama_spesialisasi,
	hari,
	waktu_mulai,
	waktu_selesai,
FROM
	users INNER JOIN jadwal_dokter
	ON users.id_user = jadwal_dokter.id_dokter
	INNER JOIN jadwal
	ON jadwal.id_jadwal = jadwal_dokter.id_jadwal
	INNER JOIN spesialisasi_dokter
	ON spesialisasi_dokter.id_dokter = users.id_user
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi);
	

CREATE VIEW daftar_dokter AS
(SELECT
	nama,
	nama_spesialisasi
FROM
	users INNER JOIN spesialisasi_dokter
	ON users.id_user = spesialisasi_dokter.id_dokter
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi);

--SELECT
SELECT * FROM lihat_jadwal_dokter;
SELECT * FROM daftar_dokter;
