package com.example.distributedlockredisson.controller

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.domain.repository.StorageRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/storage")
class StorageController(
    private val storageRepository: StorageRepository
) {

    @GetMapping("/{id}")
    fun test(@PathVariable(name = "id") id: Long): Storage? {
        val findById = storageRepository.findById(id)
        return findById
    }

}
