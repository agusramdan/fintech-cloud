package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.Transfer;
import ramdan.project.fintech.transfer.repository.AccountRepositry;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.TransactionRepository;

@Controller
public class TransferController {

    @Autowired
    private AccountRepositry accountRepositry;

    @Autowired
    private TransactionRepository transferRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Transactional(readOnly = true)
    public Double balance(String accountNumber){
        val account = accountRepositry.getOne(accountNumber);
        return account.getBalance();
    }
    @Transactional()
    public ResponseEntity<Transfer> transfer (Transfer transfer){

        val source = accountRepositry.getOne(transfer.getSource());
        source.setBalance(source.getBalance()-transfer.getAmount());
        accountRepositry.save(source);

        val beneficiary = accountRepositry.getOne(transfer.getBeneficiary());
        beneficiary.setBalance(beneficiary.getBalance()+transfer.getAmount());
        accountRepositry.save(beneficiary);

        transfer.setStatus(Status.SUCCESS);

        return ResponseEntity.ok(transfer);
    }
}
