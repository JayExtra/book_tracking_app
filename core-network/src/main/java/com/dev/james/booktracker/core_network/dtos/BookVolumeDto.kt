package com.dev.james.booktracker.core_network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookVolumeDto(
    @Json(name = "kind") val kind: String,
    @Json(name = "totalItems") val totalItems: Int,
    @Json(name = "items") val items: List<BookDto>
)
@JsonClass(generateAdapter = true)
data class BookDto(
    @Json(name = "id") val id: String,
    @Json(name = "etag") val etag: String,
    @Json(name = "selfLink") val selfLink: String,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfo
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    @Json(name = "title") val title: String,
    @Json(name = "subtitle") val subtitle: String?,
    @Json(name = "authors") val authors: List<String>,
    @Json(name = "publisher") val publisher: String?,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "description") val description: String,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "industryIdentifiers") val industryIdentifiers: List<Identifiers>,
    @Json(name = "printType") val printType: String,
    @Json(name = "categories") val categories: List<String>?,
    @Json(name = "imageLinks") val imageLinks: ImageLinks?

)

@JsonClass(generateAdapter = true)
data class Identifiers(
    @Json(name = "type") val type: String,
    @Json(name = "identifier") val identifier: String
)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    @Json(name = "smallThumbnail") val smallThumbnail: String,
    @Json(name = "thumbnail") val thumbnail: String
)
