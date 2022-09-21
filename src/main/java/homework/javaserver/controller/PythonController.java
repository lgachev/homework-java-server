package homework.javaserver.controller;

import homework.javaserver.service.PythonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PythonController {

    @Autowired
    private PythonService pythonService;

    @PostMapping("/rgb")
    public String postRGB(@RequestBody PythonDTO pythonDTO) throws Exception {
        pythonService.updateFrontendCss(pythonDTO);
        return "OK!";
    }
}
