package es.unir.relatosdepapel.springcloud.gateway.fallback;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/payments")
    public ResponseEntity<String> paymentsFallback() {
        return ResponseEntity.status(503).body("El servicio de pagos no está disponible. Por favor, intente más tarde.");
    }

    @GetMapping("/fallback/books")
    public ResponseEntity<String> booksFallback() {
        return ResponseEntity.status(503).body("El servicio de libros no está disponible. Por favor, intente más tarde.");
    }
}
