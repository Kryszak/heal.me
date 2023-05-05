package io.github.kryszak.healme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HealmeApplication

fun main(args: Array<String>) {
    runApplication<HealmeApplication>(*args)
}
