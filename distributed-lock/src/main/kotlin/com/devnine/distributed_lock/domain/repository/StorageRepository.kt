package com.devnine.distributed_lock.domain.repository

import com.devnine.distributed_lock.domain.entity.Storage
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository


interface StorageRepository : CoroutineCrudRepository<Storage, Long> {

    @Query("SELECT * FROM storages WHERE id = :id FOR UPDATE")
    suspend fun findByIdForUpdate(id: Long): Storage?
}