package com.example.spesialisRPL.Admin;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    //Ntah kenapa ini gk kebaca
    @MockBean
    private AdminRepository adminRepository;

    //Test cek id yang melakukan Pembayaran
    @Test
    public void testCheckIdPembayaran() throws Exception {
        
        //Mock data
        int idPendaftaran = 1;
        List<PasienData> mockPasienData = List.of(
            new PasienData(1, "John Doe", "Dr. Smith", "Mata", "10:00", "11:00", "2024-12-20", true, false, 1)
        );

        when(adminRepository.updatePembayaran(idPendaftaran)).thenReturn(mockPasienData);

        mockMvc.perform(get("/admin/check-id_pendaftaran")
            .param("id", String.valueOf(idPendaftaran)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nama_pasien").value("John Doe"))
            .andExpect(jsonPath("$[0].status_bayar").value(true));

        verify(adminRepository, times(1)).updatePembayaran(idPendaftaran);
    }
}
