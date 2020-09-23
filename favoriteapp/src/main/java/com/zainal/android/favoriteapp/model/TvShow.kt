package com.zainal.android.favoriteapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow (
    var id: Int = 0,
    var original_name: String? = null,
    var release_date: String? = null,
    var overview: String? = null,
    var poster: String? = null,
    var backdrop: String? = null
) : Parcelable