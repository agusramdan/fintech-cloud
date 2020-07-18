package ramdan.project.fintech.transfer.mapper;

import org.mapstruct.Mapper;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.dto.DetailDto;

@Mapper(componentModel = "spring")
public interface DetailMapper extends EntityMapper<DetailDto, Detail> {

    default Detail fromNumber(final String id) {

        if (id == null) {
            return null;
        }

        return Detail.builder().number(id).build();
    }
}
