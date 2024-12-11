package com.example.spesialisRPL.Pasien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasienService {
    @Autowired
    private PasienRepository pasienRepository;

    public List<Pasien> getAllpasiens() {
        return pasienRepository.findAll();
    }

    public List<Pasien> searchPatients(String medicalRecordNumber) {
        return pasienRepository.searchByMedicalRecordNumber(medicalRecordNumber);
    }

}
