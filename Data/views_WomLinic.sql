--VIEW
CREATE VIEW lihat_jadwal_dokter AS
SELECT
	nama,
	hari,
	waktu_mulai,
	waktu_selesai
FROM
	users INNER JOIN jadwal_dokter
	ON users.id_user = jadwal_dokter.id_dokter
	INNER JOIN jadwal
	ON jadwal.id_jadwal = jadwal_dokter.id_jadwal
