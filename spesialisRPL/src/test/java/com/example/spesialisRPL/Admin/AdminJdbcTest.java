package com.example.spesialisRPL.Admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AdminJdbcTest {
    
    @Autowired
    private AdminJdbc adminJdbc;

    @Autowired JdbcTemplate jdbcTemplate;

    //Test pembayaran ke ganti di db
    @Test
    public void testUpdatePembayaran(){
        List<PasienData> updatePasien = adminJdbc.updatePembayaran(1);

        assertEquals(1, updatePasien.size());
        
        PasienData pasien = updatePasien.get(0);
        assertEquals(1, pasien.getId_pendaftaran());
        assertTrue(pasien.isStatus_bayar());

        assertEquals("John Doe", pasien.getNama());
        assertEquals("Dr. Smith", pasien.getNama_dokter());
        assertEquals("Mata", pasien.getNama_spesialisasi());

        String sql = "SELECT status_bayar FROM pendaftaran WHERE id_pendaftaran = 1";
        Integer statusBayar = jdbcTemplate.queryForObject(sql, Integer.class);

        assertEquals(1, statusBayar);
    }
}
