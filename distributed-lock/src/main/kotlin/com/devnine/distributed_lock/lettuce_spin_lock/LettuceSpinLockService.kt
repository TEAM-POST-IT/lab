package com.devnine.distributed_lock.lettuce_spin_lock

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Repository
class LettuceSpinLockService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val txAdvise: TxAdvise
) {

    private val logger = KotlinLogging.logger {}

    private fun getLock(
        key: String,
        timeout: Long = 3000L,
    ): Boolean = redisTemplate
        .opsForValue()
        .setIfAbsent(key, "lock", Duration.ofMillis(timeout)) ?: true

    private fun unLock(key: String) = redisTemplate.delete(key)

    suspend fun <R> lock(
        lockName: String,
        key: String,
        timeoutMills: Long = 3000L,
        numForTest: Int, // 테스트를 위한 num
        func: suspend () -> R
    ): R {
        val lockKey = lockName + key

        while (!getLock(key = lockKey, timeout = timeoutMills)) {
            delay(100)
        }

        try {
            logger.info { "$numForTest Lock 획득" }
            return txAdvise.requireNew {
                func()
            }
        } finally {
            unLock(lockKey)
            logger.info { "$numForTest Lock 해제" }
        }
    }


}

@Component
class TxAdvise {

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 2)
    suspend fun <T> requireNew(func: suspend () -> T): T = func()
}