package com.gurpster.database

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlin.reflect.full.createInstance

interface Dao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: T): Int

    @Delete
    suspend fun delete(entity: T)

    @Transaction
    suspend fun insertOrUpdate(entity: T): Long {
        var id = insert(entity)
        if (id == -1L) {
            id = update(entity).toLong()
        }
        return id
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity: T): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity: T): Int

    @Delete
    suspend fun delete(vararg entityList: T)

    @Transaction
    suspend fun insertOrUpdate(vararg entityList: T) = insert(*entityList)
        .withIndex()
        .filter { it.value == -1L }
        .forEach { update(entityList[it.index]) }

    suspend fun nativeQuery(query: String): List<T> = nativeQuery(
        SimpleSQLiteQuery(query)
    )

    suspend fun nativeQuery(query: String, vararg params: Any): List<T> = nativeQuery(
        SimpleSQLiteQuery(query, params)
    )

    @RawQuery
    suspend fun nativeQuery(query: SupportSQLiteQuery): List<T>

}

suspend inline fun <reified T : Any> Dao<T>.listAll(): List<T> = nativeQuery(
    "SELECT * FROM `${T::class.java.simpleName.lowercase()}`"
).ifEmpty { arrayListOf() }

suspend inline fun <reified T : Any> Dao<T>.listAllById(id: Long): List<T> = nativeQuery(
    "SELECT * FROM `${T::class.java.simpleName.lowercase()}` WHERE id = ?", id
).ifEmpty { arrayListOf() }

suspend inline fun <reified T : Any> Dao<T>.single(id: Long): T = nativeQuery(
    "SELECT * FROM `${T::class.java.simpleName.lowercase()}` WHERE id = ? ORDER BY 'DESC' LIMIT 1",
    id
).getOrElse(0) { T::class.createInstance() }

suspend inline fun <reified T : Any> Dao<T>.deleteAll() {
    nativeQuery(
        "DELETE FROM `${T::class.java.simpleName.lowercase()}`"
    )
}

suspend inline fun <reified T : Any> Dao<T>.truncate() {
    nativeQuery(
        "TRUNCATE TABLE `${T::class.java.simpleName.lowercase()}`"
    )
}
