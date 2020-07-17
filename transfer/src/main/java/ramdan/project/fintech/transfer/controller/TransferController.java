package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.Journal;
import ramdan.project.fintech.transfer.dto.JournalDto;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.mapper.DetailMapper;
import ramdan.project.fintech.transfer.mapper.JournalMapper;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

import java.util.Date;

@RestController
public class TransferController {

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

    @Transactional(readOnly = true)
    @GetMapping ("/account/balance/{accountNumber}")
    public Double balance(@PathVariable String accountNumber) {
        val account = accountRepositry.getOne(accountNumber);
        return account.getBalance();
    }

    @Transactional()
    @PostMapping("/transfer")
    public ResponseEntity<TransferCommand> transfer(TransferCommand command) {

        val trx = Journal.builder()
                .number(command.getNo())
                .build();
        val amount = command.getAmount();
        val source = accountRepositry.getOne(command.getSource());
        source.setBalance(source.getBalance() - amount);
        accountRepositry.save(source);

        val beneficiary = accountRepositry.getOne(command.getBeneficiary());
        beneficiary.setBalance(beneficiary.getBalance() + amount);
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
                        .amount(amount * -1)
                        .build()
        );

        detailRepository.save(
                Detail.builder()
                        .number(trx.getNumber())
                        .date(trx.getDate())
                        .idx(1)
                        .account(beneficiary.getNumber())
                        .amount(amount)
                        .build()
        );
        command.setDate(trx.getDate());
        command.setStatus(Status.SUCCESS);

        return ResponseEntity.ok(command);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<JournalDto> getJournal(String number) {

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
