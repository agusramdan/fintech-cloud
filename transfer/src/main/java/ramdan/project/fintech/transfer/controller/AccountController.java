package ramdan.project.fintech.transfer.controller;

import lombok.val;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.Journal;
import ramdan.project.fintech.transfer.dto.*;
import ramdan.project.fintech.transfer.exception.OptimisticLockingFailure;
import ramdan.project.fintech.transfer.mapper.AccountMapper;
import ramdan.project.fintech.transfer.mapper.DetailMapper;
import ramdan.project.fintech.transfer.mapper.JournalMapper;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;
import ramdan.project.fintech.transfer.utils.PropertiesUtils;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class AccountController {

    @Autowired
    private AccountRepositry accountRepositry;

    @Autowired
    private AccountMapper accountMapper;

    @GetMapping("/account/{account}")
    public ResponseEntity<AccountDto> get(@PathVariable String account) {
        val result = accountMapper.toDto(accountRepositry.getOne(account));
        return ResponseEntity.ok(result);
    }
    @PostMapping("/account/create")
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto dto) {
        val account = accountMapper.toEntity(dto);
        account.setBalance(BigDecimal.ZERO);
        if(account.getLimit()==null) {
            account.setLimit(BigDecimal.ZERO);
        }
        val result = accountMapper.toDto(accountRepositry.saveAndFlush(account));
        return ResponseEntity.ok(result);
    }
    @Transactional()
    @PostMapping("/account/update")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto dto) {
        val accountUpdate = accountMapper.toEntity(dto);
        var account = accountRepositry.getOne(accountUpdate.getNumber());
        if(!account.getVersion().equals(accountUpdate.getVersion())){
            throw new OptimisticLockingFailure();
        }
        // not update balance
        accountUpdate.setBalance(null);
        account = PropertiesUtils.copyNotNullProperties(accountUpdate,account);
        account = accountRepositry.save(account);
        try{
            accountRepositry.flush();
        }
        catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockingFailure();
        }
        val result = accountMapper.toDto(account);
        return ResponseEntity.ok(result);
    }

    @Transactional(readOnly = true)
    @GetMapping("/account/history")
    public ResponseEntity<DetailDto> list(String number, Date from, Date to) {

//        val journal = journalRepository.getOne(number);
//        val journalDto = journalMapper.toDto(journal);
//
//        val details = detailRepository.findAll(Example.of(
//                Detail.builder().number(number).build())
//                , Sort.by("idx")
//        );
//
//        journalDto.setDetails(detailMapper.toDto(details.toArray(new Detail[0])));
//
//        return ResponseEntity.ok(
//                journalDto
//        );
        return null;
   }
}
