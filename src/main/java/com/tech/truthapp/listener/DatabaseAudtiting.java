package com.tech.truthapp.listener;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class DatabaseAudtiting implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        
        String uname = "system";
       // if (authentication != null) {
        //    uname = authentication.getName();
       // }
        return Optional.of(uname);
    }
}