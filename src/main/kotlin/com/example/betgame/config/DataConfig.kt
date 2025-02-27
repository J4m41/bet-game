package com.example.betgame.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.domain.AuditorAware;
import com.example.betgame.utils.AuditorAwareImpl

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class DataConfig {
    
    @Bean
    fun auditorProvider(): AuditorAware<String> = AuditorAwareImpl()
    
}