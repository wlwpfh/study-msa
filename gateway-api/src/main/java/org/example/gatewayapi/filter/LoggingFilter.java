package org.example.gatewayapi.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(LoggingFilter.Config.class);
    }
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging PRE filter: baseMessage -> {}, {}", config.getBaseMessage(), request.getRemoteAddress());
//
//            if(config.isPreLogger()) {
//                log.info("Logging PRE filter: request uri -> {}", request.getURI());
//            }
//
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if(config.isPostLogger()) {
//                    log.info("Logging POST end: response code -> {}", response.getStatusCode());
//                }
//            }));
//        });
//    }

    /**
     * 우선순위 버전
    * */
    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging PRE filter: baseMessage -> {}, {}", config.getBaseMessage(), request.getRemoteAddress());

            if(config.isPreLogger()) {
                log.info("Logging PRE filter: request uri -> {}", request.getURI());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()) {
                    log.info("Logging POST end: response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);
    }

    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
