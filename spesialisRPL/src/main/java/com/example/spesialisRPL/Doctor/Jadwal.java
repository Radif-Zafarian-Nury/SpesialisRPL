package com.example.spesialisRPL.Doctor;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Jadwal {
    private int id_jadwal;
    private String waktu_mulai;
    private String waktu_selesai;
    private Date tanggal;
}
