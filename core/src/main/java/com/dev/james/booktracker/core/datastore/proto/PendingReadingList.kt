package com.dev.james.booktracker.core.datastore.proto

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class PendingReadingList(
    @Serializable(with = PersistentListSerializer::class)
    val pendingItems : PersistentList<PendingReadingListItems> = persistentListOf()
)
