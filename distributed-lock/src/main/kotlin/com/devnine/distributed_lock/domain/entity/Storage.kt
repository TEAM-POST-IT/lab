package com.devnine.distributed_lock.domain.entity

import com.devnine.distributed_lock.domain.enum.StorageExtType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("storages")
data class Storage(
    @Id
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
