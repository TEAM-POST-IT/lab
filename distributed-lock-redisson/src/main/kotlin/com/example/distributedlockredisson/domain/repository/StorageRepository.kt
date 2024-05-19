package com.example.distributedlockredisson.domain.repository

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.domain.repository.dto.CreateStorageDto
import com.example.distributedlockredisson.jooq.tables.references.STORAGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class StorageRepository(
    private val dslContext: DSLContext
) {

    fun findById(id: Long): Storage? =
        dslContext.select()
            .from(STORAGES)
            .where(STORAGES.ID.eq(id))
            .fetchOneInto(Storage::class.java)

    fun save(createStorage: CreateStorageDto): Storage =
        dslContext.insertInto(STORAGES)
            .set(STORAGES.EXT_TYPE, createStorage.extType.name)
            .set(STORAGES.STORAGE_NAME, createStorage.storageName)
            .set(STORAGES.STORAGE_FILE_SIZE, createStorage.storageFileSize)
            .set(STORAGES.CREATED_AT, createStorage.createdAt)
            .returning()
            .fetchOneInto(Storage::class.java)!!

    /**
     * MySQL 은 UpdateQuery 에 returning 을 지원하지 않음.
     */
    fun update(storage: Storage): Int =
        dslContext.update(STORAGES)
            .set(STORAGES.STORAGE_FILE_SIZE, storage.storageFileSize)
            .set(STORAGES.UPDATED_AT, LocalDateTime.now())
            .where(STORAGES.ID.eq(storage.id))
            .execute()
}
