package ru.djets.pdd_checker.config;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.installer.NgrokVersion;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


@Getter
@Setter
@ConfigurationProperties(prefix = "telegram")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotProperties {
    final NgrokClient ngrokClient;
    final Tunnel httpTunnel;

    String botUsername;
//    String botToken;
    String botTokenPath;
    String apiUrl;
    String botPath;

    public TelegramBotProperties() {
        this.ngrokClient = new NgrokClient.Builder()
                .withJavaNgrokConfig(new JavaNgrokConfig.Builder()
                        .withNgrokVersion(NgrokVersion.V3)
                        .build())
                .build();
        this.httpTunnel = ngrokClient.connect(new CreateTunnel.Builder()
                .withProto(Proto.HTTP)
                .withAddr(8080)
                .build());
        this.botPath = httpTunnel.getPublicUrl();
    }
 }