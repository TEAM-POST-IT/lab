/*
 * This file is generated by jOOQ.
 */
package com.example.distributedlockredisson.jooq.tables


import com.example.distributedlockredisson.jooq.keys.KEY_STORAGES_PRIMARY
import com.example.distributedlockredisson.jooq.tables.records.StoragesRecord

import java.time.LocalDateTime

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Storages(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, StoragesRecord>?,
    aliased: Table<StoragesRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<StoragesRecord>(
    alias,
    com.example.distributedlockredisson.jooq.RedissonLock.`REDISSON-LOCK`,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>redisson-lock.storages</code>
         */
        val STORAGES: Storages = Storages()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<StoragesRecord> = StoragesRecord::class.java

    /**
     * The column <code>redisson-lock.storages.id</code>.
     */
    val ID: TableField<StoragesRecord, Long?> = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>redisson-lock.storages.parent_storage_id</code>.
     */
    val PARENT_STORAGE_ID: TableField<StoragesRecord, Long?> = createField(DSL.name("parent_storage_id"), SQLDataType.BIGINT, this, "")

    /**
     * The column <code>redisson-lock.storages.ext_type</code>.
     */
    val EXT_TYPE: TableField<StoragesRecord, String?> = createField(DSL.name("ext_type"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>redisson-lock.storages.storage_name</code>.
     */
    val STORAGE_NAME: TableField<StoragesRecord, String?> = createField(DSL.name("storage_name"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>redisson-lock.storages.storage_file_size</code>.
     */
    val STORAGE_FILE_SIZE: TableField<StoragesRecord, Long?> = createField(DSL.name("storage_file_size"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>redisson-lock.storages.created_at</code>.
     */
    val CREATED_AT: TableField<StoragesRecord, LocalDateTime?> = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "")

    /**
     * The column <code>redisson-lock.storages.updated_at</code>.
     */
    val UPDATED_AT: TableField<StoragesRecord, LocalDateTime?> = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(0), this, "")

    private constructor(alias: Name, aliased: Table<StoragesRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<StoragesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>redisson-lock.storages</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>redisson-lock.storages</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>redisson-lock.storages</code> table reference
     */
    constructor(): this(DSL.name("storages"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, StoragesRecord>): this(Internal.createPathAlias(child, key), child, key, STORAGES, null)
    override fun getSchema(): Schema? = if (aliased()) null else com.example.distributedlockredisson.jooq.RedissonLock.`REDISSON-LOCK`
    override fun getIdentity(): Identity<StoragesRecord, Long?> = super.getIdentity() as Identity<StoragesRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<StoragesRecord> = KEY_STORAGES_PRIMARY
    override fun `as`(alias: String): Storages = Storages(DSL.name(alias), this)
    override fun `as`(alias: Name): Storages = Storages(alias, this)
    override fun `as`(alias: Table<*>): Storages = Storages(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Storages = Storages(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Storages = Storages(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Storages = Storages(name.getQualifiedName(), null)
}
