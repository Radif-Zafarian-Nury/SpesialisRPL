DROP VIEW IF EXISTS lihat_jadwal_dokter 
--VIEW
CREATE VIEW lihat_jadwal_dokter AS
SELECT
	nama,
	hari,
	waktu_mulai,
	waktu_selesai,
	nama_spesialisasi
FROM
	users INNER JOIN jadwal_dokter
	ON users.id_user = jadwal_dokter.id_dokter
	INNER JOIN jadwal
	ON jadwal.id_jadwal = jadwal_dokter.id_jadwal
	INNER JOIN spesialisasi_dokter
	ON users.id_user = spesialisasi_dokter.id_dokter
	INNER JOIN spesialisasi
	ON spesialisasi_dokter.id_spesialisasi = spesialisasi.id_spesialisasi
