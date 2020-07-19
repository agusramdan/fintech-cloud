package ramdan.project.fintech.transfer.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(TransferController.class)
class JournalIntegrationTest {
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
