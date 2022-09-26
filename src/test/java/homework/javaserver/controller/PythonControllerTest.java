package homework.javaserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework.javaserver.controller.dto.PythonDTO;
import homework.javaserver.service.PythonService;
import homework.javaserver.socket.SessionNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PythonControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PythonService pythonService;

    @Test
    void testValid() {
        PythonDTO pythonDTO = createValidPythonDto();
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testSessionIdMissing() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setSessionId("");
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testSessionIdNull() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setSessionId(null);
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testTimestampIdNull() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setTimestamp(null);
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testBackgroundFunny() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(      0, 0,     0);");
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testBackgroundTooManyDigits() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(      0, 0000,     0);");
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testTextCssMissingColor() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssTextColorTemplate("color: rgb({red} , {blue});");
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testTextNull() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setText(null);
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testSocketSessionNotFound() throws IOException, SessionNotFoundException {
        PythonDTO pythonDTO = createValidPythonDto();
        String json = "";
        try {
            json = mapper.writeValueAsString(pythonDTO);
        } catch (JsonProcessingException e) {
            fail();
        }

        doThrow(new SessionNotFoundException("1")).when(pythonService).updateFrontendCss(Mockito.any());

        try {
            mockMvc.perform(post("/rgb").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isGone());
        } catch (Exception e) {
            fail();
        }
    }

    private PythonDTO createValidPythonDto() {
        PythonDTO pythonDTO = new PythonDTO();
        pythonDTO.setSessionId("1");
        pythonDTO.setTimestamp(1L);
        pythonDTO.setCssBackgroundColor("background-color: rgb(0, 0, 0);");
        pythonDTO.setCssTextColorTemplate("color: rgb({red}, {green}, {blue});");
        pythonDTO.setText("test");

        return pythonDTO;
    }
}
