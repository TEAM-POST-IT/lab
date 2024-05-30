package com.example.distributedlockredisson.domain.entity

import com.example.distributedlockredisson.domain.enum.StorageExtType

data class Storage(
    var id: Long = 0,
    val parentStorageId: Long? = null,
    val storageName: String,
    var storageFileSize: Long,
    val extType: StorageExtType
) {
    fun increaseFileSize() = this.apply {
        storageFileSize += 1
    }
}
