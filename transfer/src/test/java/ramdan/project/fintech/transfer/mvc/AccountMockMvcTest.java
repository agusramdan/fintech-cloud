package ramdan.project.fintech.transfer.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.domain.Account;
import ramdan.project.fintech.transfer.dto.AccountDto;
import ramdan.project.fintech.transfer.repository.AccountRepositry;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TransferController.class)
public class AccountMockMvcTest {
    @MockBean
    AccountRepositry accountRepositry;
    @Autowired
    private MockMvc mockMvc;
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
        return ow.writeValueAsString(anObject);
    }

    //    @Test
//    void account_create_success() throws Exception {
//        val input = AccountDto.builder()
//                .number("ADD-TEST-ACCOUNT")
//                .name("ADD TEST ACCOUNT")
//                .build();
//
//        given(accountRepositry.save(ArgumentMatchers.any()))
//                .willReturn(Account
//                        .builder()
//                        .number("ADD-TEST-ACCOUNT")
//                        .version(1L)
//                        .balance(BigDecimal.ZERO)
//                        .build());
//
//        mockMvc.perform(post("/account/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(input)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.balance", is(0.0)))
//        ;
//    }
//
    @Test
    void account_update1_conflict() throws Exception {
        val input = AccountDto.builder()
                .number("UPDATE-TEST-ACCOUNT")
                .name("UPDATE TEST ACCOUNT")
                .version(1L)
                .build();

        given(accountRepositry.getOne(ArgumentMatchers.any()))
                .willReturn(Account
                        .builder()
                        .number("UPDATE-TEST-ACCOUNT")
                        .name("UPDATE TEST ACCOUNT")
                        .version(2L)
                        .build());


        mockMvc.perform(post("/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isConflict())
        ;
    }

    @Test
    void account_update2_conflict() throws Exception {
        val input = AccountDto.builder()
                .number("UPDATE-TEST-ACCOUNT")
                .name("UPDATE TEST ACCOUNT")
                .version(1L)
                .build();

        given(accountRepositry.getOne(ArgumentMatchers.any()))
                .willReturn(Account
                        .builder()
                        .number("UPDATE-TEST-ACCOUNT")
                        .name("UPDATE TEST ACCOUNT")
                        .version(1L)
                        .build());
        BDDMockito.doThrow(OptimisticLockingFailureException.class)
                .when(accountRepositry).flush();

        mockMvc.perform(post("/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(input)))
                .andExpect(status().isConflict())
        ;
    }
}
