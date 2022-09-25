package homework.javaserver.controller;

import homework.javaserver.controller.dto.PythonDTO;
import homework.javaserver.controller.exception.BasicExceptionHandler;
import homework.javaserver.service.PythonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

@RestController
public class PythonController {

    private static Logger logger = LoggerFactory.getLogger(PythonController.class);

    @Autowired
    private PythonService pythonService;

    @PostMapping("/rgb")
    public String postRGB(@RequestBody @Valid PythonDTO pythonDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        pythonService.updateFrontendCss(pythonDTO);
        return "OK!";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private Map<String, String> handleIllegalArgumentException(Exception e) {
        logger.info(e.getMessage() + ": " + Arrays.toString(e.getStackTrace()));

        return BasicExceptionHandler.createExceptionBody(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
