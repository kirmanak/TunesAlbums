package com.forasoft.albums.model.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Represents a search request performed by user
 */
@Entity
data class SearchRequestEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid", index = true) val uid: Long,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "term", index = true) val term: String
) {
    override fun toString(): String {
        return "SearchRequestEntity(uid=$uid, date=$date, term='$term')"
    }
}
