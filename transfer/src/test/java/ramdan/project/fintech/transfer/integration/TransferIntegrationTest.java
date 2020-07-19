package ramdan.project.fintech.transfer.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.controller.TransferController;
import ramdan.project.fintech.transfer.dto.ReversalCommand;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ramdan.project.fintech.transfer.dto.Status.SUCCESS;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TransferController.class)
public class TransferIntegrationTest {
    @Autowired
    private MockMvc mockMvc;


    String toJson(Object anObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(anObject);
    }
    @Test
    @DisplayName("journal account history.")
    void journal_account_history() throws Exception {
        val trf = TransferCommand.builder()
                .no("TRF-001")
                .type(Type.TRANSFER)
                .source("TRF-ACCOUNT-D")
                .amount(BigDecimal.TEN)
                .beneficiary("TRF-ACCOUNT-K")
                .build();

        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(trf)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
        ;

        mockMvc.perform(get("/journal/TRF-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(10.00)))
                .andExpect(jsonPath("$.details[0].amount", is(-10.00)))
                .andExpect(jsonPath("$.details[1].amount", is(10.00)))
        ;

    }

    @Test
    @DisplayName("reversal jurnal success.")
    void reversal_journal_success() throws Exception {
        val trf = ReversalCommand.builder()
                .no("REV-TEST-001")
                .type(Type.REVERSAL)
                .source("REV-ACCOUNT-D")
                .amount(BigDecimal.TEN)
                .beneficiary("REV-ACCOUNT-K")
                .build();

        mockMvc.perform(post("/reversal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(trf)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.reversal", is("REV-TEST-001-REV")))
        ;

        mockMvc.perform(get("/account/REV-ACCOUNT-D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(110.00)))
        ;
        mockMvc.perform(get("/account/REV-ACCOUNT-K"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(190.00)))
        ;

        mockMvc.perform(get("/journal/REV-TEST-001-REV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(10.00)))
                .andExpect(jsonPath("$.details[0].amount", is(10.00)))
                .andExpect(jsonPath("$.details[1].amount", is(-10.00)))
        ;

        mockMvc.perform(get("/journal/REV-TEST-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reversal", is("REV-TEST-001-REV")))

        ;

    }


}
