package dev.thorcode.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class DetailUser(
    val id: Int,
    val username: String,
    val name: String?,
    val avatarUrl: String?,
    val following: Int,
    val followers: Int,
    val isFavorite: Boolean
) : Parcelable