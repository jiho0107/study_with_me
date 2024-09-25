package test.study.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice(basePackages = "test.study")
@RequiredArgsConstructor
@Slf4j
public class ValidationHandler extends ResponseEntityExceptionHandler {
    private final TypeMismatchConvertor typeMismatchConvertor;

    @Override // beanValidation 을 통과하지 못했을 때 Error가 담김
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        log.info("######### bean validation 통과못함1 ############");
        ex.getBindingResult().getAllErrors()
                .forEach(a -> {
                    String fieldName = ((FieldError)a).getField();
                    String message = a.getDefaultMessage();
                    map.put(fieldName,message);
                });
        log.info("######### bean validation 통과못함2 -> error 담김 ############");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @Override // TypeMismatch로 인한 오류가 발생할 때 Error가 담기게 됨
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("######### type mismatch 발생1 ############");
        TypeMismatchConvertor.Result convert = typeMismatchConvertor.convert(Objects.requireNonNull(ex.getMessage()));
        log.info("######### type mismatch 발생2 ############");
        return new ResponseEntity<>(convert,HttpStatus.BAD_REQUEST);
    }
}
