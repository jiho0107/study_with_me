package test.study.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TypeMismatchConvertor {
    private final Map<String, String> typeMismatchMessages = new HashMap<>();

    public Result convert(String originalMessage){
        int chain = originalMessage.lastIndexOf("[");
        String prefix = originalMessage.substring(chain);
        String resultString = prefix.substring(2, prefix.length() - 3);
        return new Result(resultString, typeMismatchMessages.get(resultString));
    }

    @Data
    @AllArgsConstructor
    public static class Result{
        String field;
        String description;
    }
}
