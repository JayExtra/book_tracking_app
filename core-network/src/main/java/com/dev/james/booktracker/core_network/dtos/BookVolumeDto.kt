package com.dev.james.booktracker.core_network.dtos

import com.squareup.moshi.Json

data class BookVolumeDto(
   @field:Json(name = "kind") val kind : String ,
   @field:Json(name = "totalItems")val totalItems : Int ,
   @field:Json(name = "items")val items : List<Books>
){
    data class Books(
        @field:Json(name = "id") val id : String ,
        @field:Json(name="etag") val etag : String ,
        @field:Json(name="selfLink") val selfLink : String ,
        @field:Json(name="volumeInfo") val volumeInfo : VolumeInfo
    )

    data class VolumeInfo(
        @field:Json(name = "title") val title : String ,
       //    @field:Json(name = "subtitle") val subtitle : String? ,
        @field:Json(name = "authors") val authors : List<String> ,
        @field:Json(name = "publisher") val publisher : String? ,
        @field:Json(name = "publishedDate") val publishedDate : String ,
        @field:Json(name = "description") val description : String ,
        @field:Json(name = "pageCount") val pageCount : Int ,
        @field:Json(name = "industryIdentifiers") val industryIdentifiers : List<Identifiers> ,
        @field:Json(name = "printType") val printType : String ,
        @field:Json(name = "categories") val categories : List<String>? ,
        @field:Json(name = "imageLinks") val imageLinks : ImageLinks

    )

    data class Identifiers(
        @field:Json(name = "type") val type : String ,
        @field:Json(name = "identifier") val identifier : String
    )

    data class ImageLinks(
        @field:Json(name = "smallThumbnail") val smallThumbnail : String ,
        @field:Json(name = "thumbnail") val thumbnail : String
    )
}
