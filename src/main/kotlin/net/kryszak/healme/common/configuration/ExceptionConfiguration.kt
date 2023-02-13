package net.kryszak.healme.common.configuration

import net.kryszak.healme.common.exception.ExceptionMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExceptionConfiguration {

    @Bean
    fun exceptionMapper() = ExceptionMapper()
}