package com.example.spesialisRPL.Admin;

import java.util.List;

public class JadwalDokterDataWrapper {
    private List<JadwalDokterData> listJadwal;

    public JadwalDokterDataWrapper(List<JadwalDokterData> listJadwal) {
        this.listJadwal = listJadwal;
    }

    public void addJadwal(JadwalDokterData jadwal) {
        this.listJadwal.add(jadwal);
    }

    public List<JadwalDokterData> getListJadwal() {
        return listJadwal;
    }

    public void setListJadwal(List<JadwalDokterData> listJadwal) {
        this.listJadwal = listJadwal;
    }
}