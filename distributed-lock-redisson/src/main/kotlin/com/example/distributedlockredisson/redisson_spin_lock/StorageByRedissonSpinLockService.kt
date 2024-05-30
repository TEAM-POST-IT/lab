package com.example.distributedlockredisson.redisson_spin_lock

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.domain.repository.StorageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.annotation.Transactional

@Service
class StorageByRedissonSpinLockService(
    private val storageRepository: StorageRepository,
    private val lockManager: RedissonLockManager,
) {
    fun findStorageById(id: Long): Storage =
        storageRepository.findById(id = id) ?: throw RuntimeException("Not found storage by id")

    @Transactional
    fun increaseFileSizeNonLock(id: Long): Storage =
        storageRepository.findById(id = id)
            ?.run {
                increaseFileSize()
                storageRepository.update(this)
                storageRepository.findById(id)
            } ?: throw RuntimeException("Not found storage by id")

    @Transactional
    fun increaseFileSizeDbLock(id: Long): Storage =
        storageRepository.findByIdForUpdate(id = id)
            ?.increaseFileSize()
            ?.run {
                storageRepository.update(this)
                storageRepository.findById(id)
            } ?: throw RuntimeException("Not found storage by id")

    @Transactional(propagation = NEVER)
    fun increaseFileSizeRedissonLockNonTx(
        id: Long,
        num: Int
    ): Storage =
        lockManager.lock(key = "$LOCK_NAME$id") {
            storageRepository.findById(id = id)
                ?.increaseFileSize()
                ?.run {
                    storageRepository.update(this)
                    storageRepository.findById(id)
                } ?: throw RuntimeException("Not found storage by id")
        }

    @Transactional(timeout = 2)
    fun increaseFileSizeRedissonLockWithTx(
        id: Long,
        num: Int
    ): Storage =
        lockManager.lock(key = "$LOCK_NAME$id") {
            storageRepository.findById(id = id)
                ?.increaseFileSize()
                ?.run {
                    storageRepository.update(this)
                    storageRepository.findById(id)
                } ?: throw RuntimeException("Not found storage by id")
        }


    companion object {
        private const val LOCK_NAME = "STORAGE::"
    }
}
