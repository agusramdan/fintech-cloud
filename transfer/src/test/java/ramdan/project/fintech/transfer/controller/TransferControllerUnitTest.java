package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ramdan.project.fintech.transfer.domain.Account;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransferControllerUnitTest {

    @Mock
    AccountRepositry accountRepositry;
    @Mock
    JournalRepository journalRepository;
    @Mock
    DetailRepository detailRepository;

    @InjectMocks
    TransferController controller;

    @Test
    @DisplayName("TransferCommand data success")
    void transfer_Data_success() {
        //val controller = new TransferController();
        given(accountRepositry.getOne("123456789"))
                .willReturn(Account
                        .builder()
                        .balance(BigDecimal.TEN)
                        .build());
        given(accountRepositry.getOne("234567891"))
                .willReturn(Account
                        .builder()
                        .balance(BigDecimal.TEN)
                        .build());

        val input = TransferCommand.builder()
                .no("TEST-1")
                .date(new Date())
                .type(Type.TRANSFER)
                .source("123456789")
                .beneficiary("234567891")
                .amount(BigDecimal.TEN)
                .build();

        val result = controller.transfer(input);

        assertEquals(TransferCommand.builder()
                        .no("TEST-1")
                        .date(input.getDate())
                        .type(Type.TRANSFER)
                        .source("123456789")
                        .beneficiary("234567891")
                        .amount(BigDecimal.TEN)
                        .status(Status.SUCCESS)
                        .build()
                , result.getBody());
    }


}
