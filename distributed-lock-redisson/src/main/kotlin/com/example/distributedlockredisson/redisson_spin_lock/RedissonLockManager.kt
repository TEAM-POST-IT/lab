package com.example.distributedlockredisson.redisson_spin_lock

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
class RedissonLockManager(
    private val redissonClient: RedissonClient,
    private val txAdvise: TxAdvise
) {

    /**
     * @param [key] 락의 이름
     * @param [timeUnit] 락의 시간 단위
     * @param [waitTime] 락을 기다리는 시간 (default - 5s)
     * @param [leaseTime] 락 임대 시간 (default - 3s)
     */
    fun <R> lock(
        key: String,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        waitTime: Long = 5000L,
        leaseTime: Long = 3000L,
        func: () -> R
    ): R {

        val available = redissonClient.getLock(key)
            .tryLock(
                waitTime,
                leaseTime,
                timeUnit
            )

        if (!available) {
            println("lock 획득 실패")
            throw RuntimeException("Can't lock")
        }

        try {
            println("lock 획득 시도 " + key)
            return txAdvise.requireNew {
                func()
            }
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            try {
                println("lock 해제")
                unlock(key)
            } catch (e: IllegalMonitorStateException) {
                println("IllegalMonitorStateException")
            }
        }
    }

    private fun unlock(key: String) {
        redissonClient.getLock(key).unlock()
    }

}

@Component
class TxAdvise {

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 2)
    fun <T> requireNew(func: () -> T): T = func()
}
