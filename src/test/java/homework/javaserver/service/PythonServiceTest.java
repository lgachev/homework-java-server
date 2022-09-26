package homework.javaserver.service;

import homework.javaserver.controller.dto.PythonDTO;
import homework.javaserver.socket.SessionNotFoundException;
import homework.javaserver.socket.WSHandler;
import homework.javaserver.socket.dto.CSSMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PythonServiceTest {

    @Autowired
    PythonService pythonService;

    @MockBean
    private WSHandler wsHandler;

    @Test
    void testValidRequest() {
        PythonDTO validPythonDto = createValidPythonDto();
        try {
            ArgumentCaptor<CSSMessage> captor = ArgumentCaptor.forClass(CSSMessage.class);
            pythonService.updateFrontendCss(validPythonDto);

            verify(wsHandler, times(1)).broadcast(anyString(), captor.capture());
            CSSMessage realMessage = captor.getValue();
            assertEquals(realMessage.getCssTextColor(), "color: rgb(255, 255, 255);");
        } catch (IOException | SessionNotFoundException e) {
            fail();
        }
    }

    @Test
    void testValidRequestFunnyLookingString() {
        PythonDTO validPythonDto = createValidPythonDto();
        validPythonDto.setCssBackgroundColor("background-color: rgb(   0, 30,        0);");
        try {
            pythonService.updateFrontendCss(validPythonDto);

            verify(wsHandler, times(1)).broadcast(anyString(), any(CSSMessage.class));
        } catch (IOException | SessionNotFoundException e) {
            fail();
        }
    }

    @Test
    void testInvalidTemplateExtraRGB() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(0, 0, ); background-color: rgb(0, 0, );");
        try {
            pythonService.updateFrontendCss(pythonDTO);
        } catch (IOException | SessionNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void testInvalidTemplateMissingValue() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(0, 0, );");
        try {
            pythonService.updateFrontendCss(pythonDTO);
        } catch (IOException | SessionNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void testInvalidTemplateExtraValue() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(0, 0, 0, 0);");
        try {
            pythonService.updateFrontendCss(pythonDTO);
        } catch (IOException | SessionNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void testInvalidTemplateValueTooLarge() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(0, 0, 256);");
        try {
            pythonService.updateFrontendCss(pythonDTO);
        } catch (IOException | SessionNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void testInvalidTemplateValueTooSmall() {
        PythonDTO pythonDTO = createValidPythonDto();
        pythonDTO.setCssBackgroundColor("background-color: rgb(-1, 0, 0);");
        try {
            pythonService.updateFrontendCss(pythonDTO);
        } catch (IOException | SessionNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
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
