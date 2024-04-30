package com.devnine.distributed_lock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributedLockApplication

fun main(args: Array<String>) {
    runApplication<DistributedLockApplication>(*args)
}
