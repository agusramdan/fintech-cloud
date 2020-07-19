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
import ramdan.project.fintech.transfer.exception.*;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransferControllerUnitTest {

    @Mock
    AccountRepositry accountRepositry;
    @Mock
    JournalRepository journalRepository;
    @Mock
    DetailRepository detailRepository;

    @InjectMocks
    TransferController controller;

    @Test
    @DisplayName("Transfer Command success")
    void transfer_Data_success() {
        //val controller = new TransferController();
        given(accountRepositry.existsById(anyString()))
                .willReturn(Boolean.TRUE);
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

    @Test
    @DisplayName("Transfer zero amount invalid.")
    void transfer_zeroAmount_Rejected() {

        assertThrows(InvalidTransferAmountException.class, () ->
                controller.transfer(TransferCommand.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("123456789")
                .beneficiary("234567891")
                .amount(BigDecimal.ZERO)
                .status(Status.SUCCESS)
                .build()));
    }

    @Test
    @DisplayName("Transfer minus amount invalid.")
    void transfer_minusAmount_Rejected() {

        assertThrows(InvalidTransferAmountException.class, () ->
            controller.transfer(TransferCommand.builder()
                    .no("TEST-1")
                    .type(Type.TRANSFER)
                    .source("123456789")
                    .beneficiary("234567891")
                    .amount(BigDecimal.valueOf(-0.01))
                    .status(Status.SUCCESS)
                    .build())
        );
    }

    @Test
    @DisplayName("Transfer Source Account not found.")
    void transfer_SourceAccount_NotFound() {

        assertThrows(InvalidSourceAccountException.class, () ->
            controller.transfer(TransferCommand.builder()
                    .no("TEST-1")
                    .type(Type.TRANSFER)
                    .source("SOURCE-ACCOUNT-NOT-FOUND")
                    .beneficiary("234567891")
                    .amount(BigDecimal.valueOf(0.01))
                    .status(Status.SUCCESS)
                    .build())
        );
    }

    @Test
    @DisplayName("Transfer Beneficiary Account not found.")
    void transfer_BeneficiaryAccount_NotFound() {
        given(accountRepositry.existsById("SOURCE-ACCOUNT-FOUND"))
                .willReturn(Boolean.TRUE);
        assertThrows(InvalidBeneficiaryAccountException.class, () ->
                controller.transfer(TransferCommand.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("SOURCE-ACCOUNT-FOUND")
                .beneficiary("BENEFICIARY-ACCOUNT-NOT-FOUND")
                .amount(BigDecimal.valueOf(0.01))
                .build()));
    }

    @Test
    @DisplayName("Transfer Source Account Insufficient Funds")
    void transfer_SourceAccountInsufficientFunds_error() {
        given(accountRepositry.existsById(anyString()))
                .willReturn(Boolean.TRUE);
        given(accountRepositry.getOne("SOURCE-NOMONEY"))
                .willReturn(Account.builder()
                        .balance(BigDecimal.ZERO)
                        .overdraft(BigDecimal.ZERO)
                        .build());
        assertThrows(InsufficientFundsException.class, () ->
                controller.transfer(TransferCommand.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("SOURCE-NOMONEY")
                .beneficiary("BENEFICIARY-ACCOUNT")
                .amount(BigDecimal.valueOf(0.01))
                .build()));
    }

    @Test
    @DisplayName("Transfer Type Invalid")
    void transfer_Type_invalid(){
        // acceop only TRANSFER OR RESEND
        assertThrows(InvalidTransferTypeException.class, () ->
                controller.transfer(TransferCommand.builder()
                        .no("TEST-1")
                        .type(Type.REVERSAL)
                        .source("SOURCE-NOMONEY")
                        .beneficiary("BENEFICIARY-ACCOUNT")
                        .amount(BigDecimal.valueOf(0.01))
                        .build()));

    }

    @Test
    @DisplayName("Resend transfer when transfer accepted response success")
    void transfer_resend_already_done(){
        // acceop only TRANSFER OR RESEND
        given(journalRepository.existsById("TEST-1"))
                .willReturn(Boolean.TRUE);
        val result = controller.transfer(TransferCommand.builder()
                .no("TEST-1")
                .type(Type.RESEND)
                .source("SOURCE-NOMONEY")
                .beneficiary("BENEFICIARY-ACCOUNT")
                .amount(BigDecimal.valueOf(0.01))
                .build());

        assertEquals(Status.SUCCESS,result.getBody().getStatus());
    }

    @Test
    @DisplayName("transfer when transfer accepted response success")
    void transfer_already_done(){
        // acceop only TRANSFER OR RESEND
        given(journalRepository.existsById("TEST-1"))
                .willReturn(Boolean.TRUE);

        assertThrows(TransferDuplicateException.class,()->controller.transfer(TransferCommand.builder()
                .no("TEST-1")
                .type(Type.TRANSFER)
                .source("SOURCE-NOMONEY")
                .beneficiary("BENEFICIARY-ACCOUNT")
                .amount(BigDecimal.valueOf(0.01))
                .build()));


    }
}
