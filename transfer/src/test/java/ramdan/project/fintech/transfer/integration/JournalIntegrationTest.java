package ramdan.project.fintech.transfer.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.dto.TransferCommand;
import ramdan.project.fintech.transfer.dto.Type;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ramdan.project.fintech.transfer.dto.Status.SUCCESS;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TransferController.class)
public class JournalIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    String toJson(Object anObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(anObject);
    }

    @Test
    @DisplayName("get journal by number")
    void journal_get_data() throws Exception {

        mockMvc.perform(get("/journal/TEST-001"))
                .andExpect(status().isOk())

        ;
    }

    @Test
    @DisplayName("Journal not exist return not found error ")
    void journal_not_exist_return_not_found_error() throws Exception {

        mockMvc.perform(get("/journal/NOT-EXIST-JOURNAL"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Journal not found."))

        ;
    }
}
