package com.example.distributedlockredisson.redisson_spin_lock

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.domain.repository.StorageRepository
import org.springframework.stereotype.Service
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

//    @Transactional
//    fun increaseFileSizeDbLock(id: Long): Storage =
//        storageRepository.findByIdForUpdate(id = id)
//            ?.increaseFileSize()
//            ?.run {
//                storageRepository.save(this)
//            } ?: throw RuntimeException("Not found storage by id")
//
//    @Transactional(propagation = Propagation.NEVER)
//    fun increaseFileSizeLettuceLockNonTx(id: Long, num: Int): Storage =
//        lockService.lock(lockName = LOCK_NAME, key = id.toString(), numForTest = num) {
//            storageRepository.findById(id = id)
//                ?.increaseFileSize()
//                ?.run {
//                    storageRepository.save(this)
//                } ?: throw RuntimeException("Not found storage by id")
//        }
//
//    @Transactional(timeout = 2)
//    fun increaseFileSizeLettuceLockWithTx(id: Long, num: Int): Storage =
//        lockService.lock(lockName = LOCK_NAME, key = id.toString(), numForTest = num) {
//            storageRepository.findById(id = id)
//                ?.increaseFileSize()
//                ?.run {
//                    storageRepository.save(this)
//                } ?: throw RuntimeException("Not found storage by id")
//        }


    companion object {
        private const val LOCK_NAME = "STORAGE::"
    }
}
