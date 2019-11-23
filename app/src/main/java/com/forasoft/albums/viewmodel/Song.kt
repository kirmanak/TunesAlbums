package com.forasoft.albums.viewmodel

import java.text.SimpleDateFormat
import java.util.*

data class Song(
    val songName: String,
    val trackTimeMs: Long,
    val previewUrl: String
) {

    fun formatDuration(): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(trackTimeMs))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (songName != other.songName) return false
        if (trackTimeMs != other.trackTimeMs) return false
        if (previewUrl != other.previewUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = songName.hashCode()
        result = 31 * result + trackTimeMs.hashCode()
        result = 31 * result + previewUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "Song(songName='$songName', trackTimeMs=$trackTimeMs, previewUrl='$previewUrl')"
    }
}