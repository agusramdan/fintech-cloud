package ramdan.project.fintech.transfer.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.dto.JournalDto;
import ramdan.project.fintech.transfer.exception.JournalNotFoundException;
import ramdan.project.fintech.transfer.mapper.DetailMapper;
import ramdan.project.fintech.transfer.mapper.JournalMapper;
import ramdan.project.fintech.transfer.repository.DetailRepository;
import ramdan.project.fintech.transfer.repository.JournalRepository;

@RestController
public class JournalController {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalMapper journalMapper;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private DetailMapper detailMapper;

    @Transactional(readOnly = true)
    @GetMapping("/journal/{number}")
    public ResponseEntity<JournalDto> getJournal(@PathVariable String number) {

        if(!journalRepository.existsById(number)){
            throw new JournalNotFoundException();
        }
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
