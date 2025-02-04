package es.unir.relatosdepapel.springcloud.gateway.filter;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PostFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.POST) {
            String targetMethod = exchange.getRequest().getHeaders().getFirst("X-Http-Method-Override");

            if (targetMethod != null && !targetMethod.equalsIgnoreCase("POST")) {
                exchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
                exchange.getResponse().getHeaders().add("Content-Type", "application/json");
                return exchange.getResponse().writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(
                                ("{\"error\":\"POST method cannot be overridden to " + targetMethod + ". Method not allowed.\"}").getBytes()
                        ))
                );
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}