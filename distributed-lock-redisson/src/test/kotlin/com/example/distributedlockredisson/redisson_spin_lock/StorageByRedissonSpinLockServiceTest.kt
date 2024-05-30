package com.example.distributedlockredisson.redisson_spin_lock

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
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

        "db lock" {
            val size = 100
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize
            val executorService = Executors.newFixedThreadPool(size)
            val countDownLatch = CountDownLatch(size)

            for (i in 1..size) {
                executorService.execute {
                    storageService.increaseFileSizeDbLock(id = storageId)
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }

        "redisson spin lock non tx" {
            val size = 100
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize
            val executorService = Executors.newFixedThreadPool(size)
            val countDownLatch = CountDownLatch(size)

            for (num in 1..size) {
                executorService.execute {
                    storageService.increaseFileSizeRedissonLockNonTx(id = storageId, num = num)
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }

        /*
        동시 요청 수 == DB 커넥션 수 일경우 deadlock
        ex) 동시 요청 수 == DB 커넥션 수(maximum-pool-size) == 10
        1. 첫 요청 10개 (잔여 커넥션 0) -> 그 중 하나(A 커넥션)가 Lock 획득
        2. A 커넥션이 Require New 트랜잭션을 위해 커넥션 get 시도 -> 잔여 커넥션 0
        3. Lock Timeout에 따라 다른 요청들이 Lock을 획득은 하지만 A 커넥션도 커넥션을 놔주지 않았으므로 계속 해서 커넥션 0, 무한대기

        결론: DB 커넥션 풀 여유가 있어야 함.
         */
        "Redisson spin lock with tx".config(enabled = false) {
            val size = 10
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize
            val executorService = Executors.newFixedThreadPool(size)
            val countDownLatch = CountDownLatch(size)

            for (num in 1..size) {
                executorService.execute {
                    storageService.increaseFileSizeRedissonLockWithTx(id = storageId, num = num)
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }

    }
}
