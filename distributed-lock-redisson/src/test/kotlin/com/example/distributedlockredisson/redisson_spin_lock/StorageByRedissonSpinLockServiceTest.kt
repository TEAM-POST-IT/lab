package com.example.distributedlockredisson.redisson_spin_lock

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
internal class StorageByRedissonSpinLockServiceTest(
    private val storageService: StorageByRedissonSpinLockService
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)
    private val storageId = 1L
    private val logger = KotlinLogging.logger { }

    /*
    파일 사이즈 증가 동시성 테스트
     */
    init {
        "non lock" {
            val size = 100
            val executorService = Executors.newFixedThreadPool(size)
            val countDownLatch = CountDownLatch(size)
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize

            for (i in 1..size) {
                executorService.execute {
                    storageService.increaseFileSizeNonLock(1)
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }
        }
    }
}
