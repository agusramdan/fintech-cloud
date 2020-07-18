package ramdan.project.fintech.transfer.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ramdan.project.fintech.transfer.dto.Status.SUCCESS;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.controller.TransferController;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TransferController.class)
public class TransferMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    AccountRepositry accountRepositry;
//    @MockBean
//    JournalRepository journalRepository;
//    @MockBean
//    DetailRepository detailRepository;
    //@MockBean
    //private SendMoneyUseCase sendMoneyUseCase;

    String toJson(Object anObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(anObject );
    }
    @Test
    void testSendMoney() throws Exception {
        val input = TransferCommand.builder()
                .no("MOCK-TEST-1")
                .type(Type.TRANSFER)
                .source("123456789")
                .beneficiary("234567891")
                .amount(BigDecimal.TEN)
                .build();

        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(SUCCESS.name())))
        ;
    }

}
