package dev.thorcode.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class User(
    val id: Int,
    val username: String,
    val avatarUrl: String?,
    val isFavorite: Boolean = false
) : Parcelable
