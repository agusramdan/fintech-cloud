package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ramdan.project.fintech.transfer.domain.Account;
import ramdan.project.fintech.transfer.dto.AccountDto;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;
import ramdan.project.fintech.transfer.mapper.AccountMapper;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerUnitTest {

//    @Mock
//    AccountRepositry accountRepositry;
//
//    @Mock
//    AccountMapper accountMapper;
//
//    @InjectMocks
//    AccountController controller;
//
//    @Test
//    @DisplayName("Account create ")
//    void account_create_success(){
//        //val controller = new TransferController();
//        given(accountRepositry.save(ArgumentMatchers.any()))
//                .willReturn(Account
//                        .builder()
//                        .number("ADD-TEST-ACCOUNT")
//                        .balance(0.0)
//                        .build());
//
//        val input = AccountDto.builder()
//                .number("ADD-TEST-ACCOUNT")
//                .name("ACCOUNT TEST ACC")
//                .build();
//
//        val result = controller.create(input);
//
//        assertThat(result.getBody(),
//                allOf(
//                        hasProperty("nummber",is("ADD-TEST-ACCOUNT")),
//                        hasProperty("balance",is(0.0))
//                )
//
//        );
//
//    }



}
