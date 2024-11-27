package com.example.spesialisRPL.User;

import lombok.Data;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class UserData {

    @NotEmpty(message = "Nama tidak boleh kosong")
    private String nama;

    @NotEmpty(message = "NIK tidak boleh kosong")
    @Size(min = 16, max = 16, message = "NIK harus 16 karakter")
    private String nik;

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email tidak valid")
    private String email;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String alamat;

    @NotEmpty(message = "Kata sandi tidak boleh kosong")
    @Size(min = 6, max = 20, message = "Kata sandi harus 6-20 karakter")
    private String kata_sandi;
    
    private String jenis_kelamin;
    private String peran;

    @Transient
    private String confpassword;
}
