package ramdan.project.fintech.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.dto.AccountDto;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    String toJson(Object anObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(anObject);
    }

    @Test
    @DisplayName("Create account success")
    void create_account_success() throws Exception {
        val input = AccountDto.builder()
                .number("ADD-INT-TEST-ACCOUNT")
                .name("ADD INT TEST ACCOUNT")
                .build();

        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", is(0)))
                .andExpect(jsonPath("$.balance", is(0)))
                .andExpect(jsonPath("$.limit", is(0)))
        ;

    }

    @Test
    @DisplayName("Create account have limit credit success")
    void create_accountHaveLimitCredit_success() throws Exception {
        val input = AccountDto.builder()
                .number("ADD-INT-TEST-ACC-LIMIT")
                .name("ADD INT TEST ACC LIMIT")
                .limit(BigDecimal.valueOf(100000.00))
                .build();

        mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", is(100000.00)))
        ;

    }

    @Test
    void update_account_success() throws Exception {

        mockMvc.perform(get("/account/UPDATE-INT-TEST-ACCOUNT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", is(3)))
                .andExpect(jsonPath("$.name", blankOrNullString()))
                .andExpect(jsonPath("$.limit", is(100000000.00)))
        ;

        val input = AccountDto.builder()
                .number("UPDATE-INT-TEST-ACCOUNT")
                .name("NAME UPDATE")
                .version(3L)
                .build();

        mockMvc.perform(post("/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", is(4)))
                .andExpect(jsonPath("$.name", is("NAME UPDATE")))
                .andExpect(jsonPath("$.limit", is(100000000.00)))
        ;

    }

    @Test
    void update_account_fail() throws Exception {

        val input = AccountDto.builder()
                .number("UPDATE-TEST-FAIL")
                .version(2L)
                .build();

        mockMvc.perform(post("/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isConflict())
        //.andExpect(jsonPath("$.version", is(4)))
        //.andExpect(jsonPath("$.name", is("NAME UPDATE")))
        //.andExpect(jsonPath("$.limit", is(100000000.00)))
        ;

    }

    @Test
    void update_account_BalanceNotChange() throws Exception {

        val input = AccountDto.builder()
                .number("UPDATE-INT-BAL-NOT-CHANGE")
                .balance(BigDecimal.valueOf(12345678.00))
                .version(3L)
                .build();

        mockMvc.perform(post("/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(11.00)))
        //.andExpect(jsonPath("$.name", is("NAME UPDATE")))
        //.andExpect(jsonPath("$.limit", is(100000000.00)))
        ;

    }

    @Test
    void list_account_history() throws Exception {

        mockMvc.perform(get("/account/history/HISTORY-ACCOUNT-D")
                .param("from", "2020-01-01")
                .param("to", "2020-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(-10.00)))
                .andExpect(jsonPath("$[0].balance", is(90.00)))
        ;

        mockMvc.perform(get("/account/history/HISTORY-ACCOUNT-K")
                .param("from", "2020-01-01")
                .param("to", "2020-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(10.00)))
                .andExpect(jsonPath("$[0].balance", is(110.00)))
        ;

    }

    @Test
    void transfer_account_history() throws Exception {
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
}
