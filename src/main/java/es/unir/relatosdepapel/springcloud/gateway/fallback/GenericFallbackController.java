package es.unir.relatosdepapel.springcloud.gateway.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class GenericFallbackController {

    @GetMapping("/not-found")
    public ResponseEntity<Object> handleFallback() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", "The requested route is not configured in the API Gateway.");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
