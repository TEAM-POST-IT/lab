package com.example.distributedlockredisson.domain.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class StorageRepository(
    private val dslContext: DSLContext
) {

    fun save() {
        TODO()
    }
}
