package com.example.distributedlockredisson.controller

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.domain.enum.StorageExtType
import com.example.distributedlockredisson.domain.repository.StorageRepository
import com.example.distributedlockredisson.domain.repository.dto.CreateStorageDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/storage")
class StorageController(
    private val storageRepository: StorageRepository
) {

    @GetMapping("/{id}")
    fun select(@PathVariable(name = "id") id: Long): Storage? =
        storageRepository.findById(id)

    @PostMapping
    fun create(): Storage = storageRepository.save(
        CreateStorageDto(
            storageName = "test_storage",
            storageFileSize = 1L,
            extType = StorageExtType.FILE,
            createdAt = LocalDateTime.now()
        )
    )

    @PutMapping("/{id}")
    fun update(@PathVariable(name = "id") id: Long): Storage? =
        storageRepository.update(
            storageRepository.findById(id)!!.increaseFileSize()
        ).run {
            storageRepository.findById(id)
        }
}
