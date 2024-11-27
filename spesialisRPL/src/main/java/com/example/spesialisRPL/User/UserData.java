package com.example.spesialisRPL.User;

import lombok.Data;
import jakarta.persistence.Transient;

@Data
public class UserData {
    private String nama;
    private String nik;
    private String email;
    private String alamat;
    private String kata_sandi;
    private String jenis_kelamin;

    @Transient
    private String confpassword;
}
