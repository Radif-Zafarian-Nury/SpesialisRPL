package com.example.spesialisRPL.RekamMedis;

import java.util.List;

public interface RekamMedisRepository {
    List<RekamMedis> findByIdPasien(int idPasien);
    RekamMedis save(RekamMedis rekamMedis);
}
