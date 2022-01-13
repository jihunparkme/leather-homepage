package com.leather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LeatherApplication

fun main(args: Array<String>) {
	runApplication<LeatherApplication>(*args)
}
