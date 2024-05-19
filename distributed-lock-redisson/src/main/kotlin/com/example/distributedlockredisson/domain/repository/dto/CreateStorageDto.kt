package com.example.distributedlockredisson.domain.repository.dto

import com.example.distributedlockredisson.domain.enum.StorageExtType
import java.time.LocalDateTime

data class CreateStorageDto(
    val storageName: String,
    var storageFileSize: Long,
    val extType: StorageExtType,
    val createdAt: LocalDateTime
)
