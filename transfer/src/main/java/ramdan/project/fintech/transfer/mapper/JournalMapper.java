package ramdan.project.fintech.transfer.mapper;

import org.mapstruct.Mapper;
import ramdan.project.fintech.transfer.domain.Journal;
import ramdan.project.fintech.transfer.dto.JournalDto;

@Mapper(componentModel = "spring")
public interface JournalMapper extends EntityMapper<JournalDto, Journal> {

    //JournalMapper INSTANCE = Mappers.getMapper( JournalMapper.class );
    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //JournalDto transferToTransactionDto(Journal transfer);

    //Journal commandToJournal(TransferCommand command);
    default Journal fromNumber(final String id) {

        if (id == null) {
            return null;
        }
        return Journal.builder().number(id).build();
    }
}
