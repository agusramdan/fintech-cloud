package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.Journal;
import ramdan.project.fintech.transfer.dto.JournalDto;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.exception.InsufficientFundsException;
import ramdan.project.fintech.transfer.exception.InvalidBeneficiaryAccountException;
import ramdan.project.fintech.transfer.exception.InvalidSourceAccountException;
import ramdan.project.fintech.transfer.exception.InvalidTransferAmountException;
import ramdan.project.fintech.transfer.mapper.DetailMapper;
import ramdan.project.fintech.transfer.mapper.JournalMapper;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class TransferController {

    private static BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);
    @Autowired
    private AccountRepositry accountRepositry;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalMapper journalMapper;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private DetailMapper detailMapper;

    @Transactional()
    @PostMapping("/transfer")
    public ResponseEntity<TransferCommand> transfer(@RequestBody TransferCommand command) {

        val amount = command.getAmount();
        if(amount.doubleValue()<= 0.0){
            throw new InvalidTransferAmountException();
        }
        if(!accountRepositry.existsById(command.getSource())){
            throw new InvalidSourceAccountException();
        }
        if(!accountRepositry.existsById(command.getBeneficiary())){
            throw new InvalidBeneficiaryAccountException();
        }
        val trx = Journal.builder()
                .number(command.getNo())
                .amount(amount)
                .remark1(command.getRemark1())
                .remark2(command.getRemark2())
                .build();

        val source = accountRepositry.getOne(command.getSource());
        double availableBalance = source.getBalance().doubleValue();
        if(availableBalance < amount.doubleValue()){
            throw new InsufficientFundsException();
        }

        source.setBalance(source.getBalance().subtract(amount));
        accountRepositry.save(source);

        val beneficiary = accountRepositry.getOne(command.getBeneficiary());
        beneficiary.setBalance(beneficiary.getBalance().add(amount));
        accountRepositry.save(beneficiary);

        if (trx.getDate() == null) {
            trx.setDate(new Date());
        }

        journalRepository.save(trx);
        detailRepository.save(
                Detail.builder()
                        .number(trx.getNumber())
                        .date(trx.getDate())
                        .idx(0)
                        .account(source.getNumber())
                        .amount(amount.multiply(MINUS_ONE))
                        .balance(source.getBalance())
                        .remark1(trx.getRemark1())
                        .remark2(trx.getRemark2())
                        .build()
        );

        detailRepository.save(
                Detail.builder()
                        .number(trx.getNumber())
                        .date(trx.getDate())
                        .idx(1)
                        .account(beneficiary.getNumber())
                        .amount(amount)
                        .balance(beneficiary.getBalance())
                        .remark1(trx.getRemark1())
                        .remark2(trx.getRemark2())
                        .build()
        );
        command.setDate(trx.getDate());
        command.setStatus(Status.SUCCESS);

        return ResponseEntity.ok(command);
    }

    @Transactional(readOnly = true)
    @GetMapping("/journal/{number}")
    public ResponseEntity<JournalDto> getJournal(@PathVariable String number) {

        val journal = journalRepository.getOne(number);
        val journalDto = journalMapper.toDto(journal);

        val details = detailRepository.findAll(Example.of(
                Detail.builder().number(number).build())
                , Sort.by("idx")
        );

        journalDto.setDetails(detailMapper.toDto(details.toArray(new Detail[0])));

        return ResponseEntity.ok(
                journalDto
        );
    }
}
