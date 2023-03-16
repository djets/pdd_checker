package ru.djets.pdd_checker.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WebClientConfiguration {
    TelegramBotProperties properties;

    public int TIMEOUT = 1000;

    @Bean
    public WebClient webClientWithTimeout() {
        return WebClient.builder()
                .baseUrl(properties.getApiUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient
                        .create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                        .doOnConnected(connection -> {
                            connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                                })
                ))
                .build();
    }
}
