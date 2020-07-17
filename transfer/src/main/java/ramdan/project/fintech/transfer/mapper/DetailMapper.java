package ramdan.project.fintech.transfer.mapper;

import org.mapstruct.Mapper;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.Journal;
import ramdan.project.fintech.transfer.dto.DetailDto;

@Mapper(componentModel = "spring")
public interface DetailMapper extends EntityMapper<DetailDto, Detail> {

    default Journal fromNumber(final String id) {

        if (id == null) {
            return null;
        }

        return Journal.builder().number(id).build();
    }
}
