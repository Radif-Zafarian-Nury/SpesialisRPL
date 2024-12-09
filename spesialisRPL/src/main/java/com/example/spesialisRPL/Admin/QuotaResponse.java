package com.example.spesialisRPL.Admin;

public class QuotaResponse {
    private int kuotaTerisi;
    private int kuotaMax;

    public QuotaResponse(int kuotaTerisi, int kuotaMax){
        this.kuotaTerisi = kuotaTerisi;
        this.kuotaMax = kuotaMax;
    }

    public int getKuotaTerisi() {
        return kuotaTerisi;
    }

    public void setKuotaTerisi(int kuotaTerisi) {
        this.kuotaTerisi = kuotaTerisi;
    }

    public int getKuotaMax() {
        return kuotaMax;
    }

    public void setKuotaMax(int kuotaMax) {
        this.kuotaMax = kuotaMax;
    }

}
