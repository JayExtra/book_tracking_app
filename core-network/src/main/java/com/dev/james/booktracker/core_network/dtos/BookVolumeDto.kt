package com.dev.james.booktracker.core_network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookVolumeDto(
    @Json(name = "kind") val kind: String? = null,
    @Json(name = "totalItems") val totalItems: Int ?= null,
    @Json(name = "items") val items: List<BookDto>? = null
)
@JsonClass(generateAdapter = true)
data class BookDto(
    @Json(name = "id") val id: String? = null ,
    @Json(name = "etag") val etag: String? = null,
    @Json(name = "selfLink") val selfLink: String? = null,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfo? = null
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    @Json(name = "title") val title: String? = null,
    @Json(name = "subtitle") val subtitle: String? = null,
    @Json(name = "authors") val authors: List<String>? = null,
    @Json(name = "publisher") val publisher: String? = null,
    @Json(name = "publishedDate") val published_date: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "pageCount") val pageCount: Int? = null,
    @Json(name = "industryIdentifiers") val industry_identifiers: List<Identifiers>? = null,
    @Json(name = "printType") val print_type: String? = null,
    @Json(name = "categories") val categories: List<String>? = null,
    @Json(name = "imageLinks") val image_links: ImageLinks? = null

)

@JsonClass(generateAdapter = true)
data class Identifiers(
    @Json(name = "type") val type: String? = null,
    @Json(name = "identifier") val identifier: String? = null
)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    @Json(name = "smallThumbnail") val small_thumbnail: String? = null,
    @Json(name = "thumbnail") val thumbnail: String? = null
)
