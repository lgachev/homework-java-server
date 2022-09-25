package homework.javaserver.controller.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasicExceptionHandler {

    public static Map<String, String> createExceptionBody(String exceptionMessage,
                                                          HttpStatus exceptionType) {
        Map<String, String> result = new HashMap<>();
        result.put("timestamp", new Date().toString());
        result.put("message", exceptionMessage);
        result.put("status", "" + exceptionType.value());

        return result;
    }
}
