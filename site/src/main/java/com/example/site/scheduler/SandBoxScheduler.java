package com.example.site.scheduler;

import com.example.site.service.impl.SandboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SandBoxScheduler {

    private final SandboxService service;

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(fixedRate = 300000)
    public void sandbox() {
        try {
            log.info("Sandbox clear started");

            service.closeSandBox();
            log.info("Sandbox closed");

            service.executeClearSandBox();
        } finally {
            service.openSandbox();
            log.info("Sandbox opened");
            log.info("Sandbox clear");
        }
    }
}
