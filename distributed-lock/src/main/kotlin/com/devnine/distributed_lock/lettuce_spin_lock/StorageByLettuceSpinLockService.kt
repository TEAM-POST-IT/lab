package com.devnine.distributed_lock.lettuce_spin_lock

import com.devnine.distributed_lock.domain.entity.Storage
import com.devnine.distributed_lock.domain.repository.StorageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StorageByLettuceSpinLockService(
    private val storageRepository: StorageRepository,
    private val lockService: LettuceSpinLockService
) {

    suspend fun findStorageById(id: Long): Storage =
        storageRepository.findById(id = id) ?: throw RuntimeException("Not found storage by id")

    @Transactional
    suspend fun increaseFileSizeNonLock(id: Long): Storage =
        storageRepository.findById(id = id)
            ?.increaseFileSize()
            ?.run {
                storageRepository.save(this)
            } ?: throw RuntimeException("Not found storage by id")

    @Transactional
    suspend fun increaseFileSizeDbLock(id: Long): Storage =
        storageRepository.findByIdForUpdate(id = id)
            ?.increaseFileSize()
            ?.run {
                storageRepository.save(this)
            } ?: throw RuntimeException("Not found storage by id")

    @Transactional(propagation = Propagation.NEVER)
    suspend fun increaseFileSizeLettuceLockNonTx(id: Long, num: Int): Storage =
        lockService.lock(lockName = LOCK_NAME, key = id.toString(), numForTest = num) {
            storageRepository.findById(id = id)
                ?.increaseFileSize()
                ?.run {
                    storageRepository.save(this)
                } ?: throw RuntimeException("Not found storage by id")
        }

    @Transactional(timeout = 2)
    suspend fun increaseFileSizeLettuceLockWithTx(id: Long, num: Int): Storage =
        lockService.lock(lockName = LOCK_NAME, key = id.toString(), numForTest = num) {
            storageRepository.findById(id = id)
                ?.increaseFileSize()
                ?.run {
                    storageRepository.save(this)
                } ?: throw RuntimeException("Not found storage by id")
        }


    companion object {
        private const val LOCK_NAME = "STORAGE::"
    }
}