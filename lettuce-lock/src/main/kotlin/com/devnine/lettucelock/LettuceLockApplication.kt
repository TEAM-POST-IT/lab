package com.devnine.lettucelock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LettuceLockApplication

fun main(args: Array<String>) {
    runApplication<LettuceLockApplication>(*args)
}
