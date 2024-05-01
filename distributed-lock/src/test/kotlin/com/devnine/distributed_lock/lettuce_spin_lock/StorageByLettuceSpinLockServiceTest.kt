package com.devnine.distributed_lock.lettuce_spin_lock

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.measureTime

@SpringBootTest
internal class StorageByLettuceSpinLockServiceTest(
    private val storageService: StorageByLettuceSpinLockService
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
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize

            coroutineScope {
                val jobs = (1..size).map {
                    async {
                        storageService.increaseFileSizeNonLock(id = storageId)
                    }
                }

                val time = measureTime { jobs.awaitAll() }
                logger.info { "time taken: $time" }
            }

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }
        }

        "db lock" {
            val size = 100
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize

            coroutineScope {
                val jobs = (1..size).map {
                    async {
                        storageService.increaseFileSizeDbLock(id = storageId)
                    }
                }

                val time = measureTime { jobs.awaitAll() }
                logger.info { "time taken: $time" }
            }

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }

        "lettuce spin lock non tx" {
            val size = 100
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize

            coroutineScope {
                val jobs = (1..size).map { num ->
                    async {
                        storageService.increaseFileSizeLettuceLockNonTx(id = storageId, num = num)
                    }
                }

                val time = measureTime { jobs.awaitAll() }
                logger.info { "time taken: $time" }
            }

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }

        /*
        동시 요청 수 == DB 커넥션 수 일경우 deadlock
        ex) 동시 요청 수 == DB 커넥션 수 == 10
        1. 첫 요청 10개 (잔여 커넥션 0) -> 그 중 하나(A 커넥션)가 Lock 획득
        2. A 커넥션이 Require New 트랜잭션을 위해 커넥션 get 시도 -> 잔여 커넥션 0
        3. Lock Timeout에 따라 다른 요청들이 Lock을 획득은 하지만 A 커넥션도 커넥션을 놔주지 않았으므로 계속 해서 커넥션 0, 무한대기
         */

        //TODO: Transactional timeout, r2dbc create connection timeout, acquire timeout(익센셥 후 종료) 조정해봐도 잘안됨..
        "lettuce spin lock with tx" {
            val size = 10
            val beforeFileSize = storageService.findStorageById(id = storageId).storageFileSize

            coroutineScope {
                val jobs = (1..size).map { num ->
                    async {
                        storageService.increaseFileSizeLettuceLockWithTx(id = storageId, num = num)
                    }
                }

                val time = measureTime { jobs.awaitAll() }
                logger.info { "time taken: $time" }
            }

            val afterFileSize = storageService.findStorageById(id = storageId).storageFileSize

            logger.info { "beforeFileSize: $beforeFileSize" }
            logger.info { "afterFileSize: $afterFileSize" }

            afterFileSize shouldBe beforeFileSize + size
        }


    }
}