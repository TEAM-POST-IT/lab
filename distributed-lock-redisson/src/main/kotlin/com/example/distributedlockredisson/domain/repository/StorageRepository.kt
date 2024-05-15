package com.example.distributedlockredisson.domain.repository

import com.example.distributedlockredisson.domain.entity.Storage
import com.example.distributedlockredisson.jooq.tables.references.STORAGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class StorageRepository(
    private val dslContext: DSLContext
) {

    fun findById(id: Long): Storage? =
        dslContext.select()
            .from(STORAGES)
            .where(STORAGES.ID.eq(id))
            .fetchOneInto(Storage::class.java)
}
