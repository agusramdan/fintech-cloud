package ramdan.project.fintech.transfer.controller;

import lombok.val;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ramdan.project.fintech.transfer.domain.Account;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.Transfer;
import ramdan.project.fintech.transfer.dto.Type;
import ramdan.project.fintech.transfer.repository.AccountRepositry;

@ExtendWith(MockitoExtension.class)
public class TransferControllerUnitTest {

    @Mock
    AccountRepositry accountRepositry;

    @InjectMocks
    TransferController controller;
    @Test
    @DisplayName("Transfer data success")
    void transfer_Data_success(){
        //val controller = new TransferController();
        given(accountRepositry.getOne("123456789"))
                .willReturn(Account
                        .builder()
                        .balance(10.0)
                        .build());
        given(accountRepositry.getOne("234567891"))
                .willReturn(Account
                        .builder()
                        .balance(10.0)
                        .build());

        val input = Transfer.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("123456789")
                .beneficiary("234567891")
                .amount(10.0)
                .build();

        val result = controller.transfer(input);

        assertEquals(Transfer.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("123456789")
                .beneficiary("234567891")
                .amount(10.0)
                .status(Status.SUCCESS)
                .build()
                ,result.getBody());
    }



}
