package net.kryszak.healme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class HealmeApplication

fun main(args: Array<String>) {
	runApplication<HealmeApplication>(*args)
}

@RestController
class Control {

	@GetMapping("/test")
	fun test() = "Hello!"
}
