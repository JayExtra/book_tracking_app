package com.dev.james.booktracker.core.datastore.proto

import com.dev.james.booktracker.core.common_models.BaseBookData
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable


@Serializable
data class WishlistItems(
    @Serializable(with = PersistentListSerializer::class)
    val wishlist : PersistentList<BaseBookData> = persistentListOf()
)
