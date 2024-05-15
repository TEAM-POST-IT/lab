package com.example.distributedlockredisson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributedLockRedissonApplication

fun main(args: Array<String>) {
    runApplication<DistributedLockRedissonApplication>(*args)
}
