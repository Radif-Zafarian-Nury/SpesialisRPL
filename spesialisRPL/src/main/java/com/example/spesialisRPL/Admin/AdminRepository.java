package com.example.spesialisRPL.Admin;

import java.util.Optional;

public interface AdminRepository {
    Optional<FormPendaftaranData> findNik(String nik);
}
