package ramdan.project.fintech.transfer.controller;

import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ramdan.project.fintech.transfer.dto.AccountDto;
import ramdan.project.fintech.transfer.dto.DetailDto;
import ramdan.project.fintech.transfer.exception.AccountOptimisticLockingFailure;
import ramdan.project.fintech.transfer.mapper.AccountMapper;
import ramdan.project.fintech.transfer.mapper.DetailMapper;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.utils.PropertiesUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountRepositry accountRepositry;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DetailRepository detailRepositry;

    @Autowired
    private DetailMapper detailMapper;

    @GetMapping("/account")
    public ResponseEntity<List<AccountDto>> list(Pageable pageable) {
        val result = accountRepositry.findAll(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-page-total-pages",String.valueOf(result.getTotalPages()))
                .header("X-page-total-elements",String.valueOf(result.getTotalElements()))
                .header("X-page-size",String.valueOf(result.getSize()))
                .header("X-page-number",String.valueOf(result.getNumber()))
                .header("X-page-sort",String.valueOf(result.getSort()))
                .body(accountMapper.toDto(result.toList()));
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<AccountDto> get(@PathVariable String account) {
        val result = accountMapper.toDto(accountRepositry.getOne(account));
        return ResponseEntity.ok(result);
    }
    @PostMapping("/account/create")
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto dto) {
        val account = accountMapper.toEntity(dto);
        account.setBalance(BigDecimal.ZERO);
        if (account.getOverdraft() == null) {
            account.setOverdraft(BigDecimal.ZERO);
        }
        val result = accountMapper.toDto(accountRepositry.saveAndFlush(account));
        return ResponseEntity.ok(result);
    }

    @Transactional()
    @PostMapping("/account/update")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto dto) {
        val accountUpdate = accountMapper.toEntity(dto);
        var account = accountRepositry.getOne(accountUpdate.getNumber());
        if (!account.getVersion().equals(accountUpdate.getVersion())) {
            throw new AccountOptimisticLockingFailure();
        }
        // not update balance
        accountUpdate.setBalance(null);
        account = PropertiesUtils.copyNotNullProperties(accountUpdate, account);
        account = accountRepositry.save(account);
        try {
            accountRepositry.flush();
        } catch (OptimisticLockingFailureException ex) {
            throw new AccountOptimisticLockingFailure();
        }
        val result = accountMapper.toDto(account);
        return ResponseEntity.ok(result);
    }

    @Transactional(readOnly = true)
    @GetMapping("/account/history/{account}")
    public ResponseEntity<List<DetailDto>> history(
            @PathVariable String account,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, Pageable pageable) {

        val result = detailRepositry.findAllByAccount(account, from, to,pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-page-total-pages",String.valueOf(result.getTotalPages()))
                .header("X-page-total-elements",String.valueOf(result.getTotalElements()))
                .header("X-page-size",String.valueOf(result.getSize()))
                .header("X-page-number",String.valueOf(result.getNumber()))
                .header("X-page-sort",String.valueOf(result.getSort()))
                .body(detailMapper.toDto(result.toList()));
    }
}
