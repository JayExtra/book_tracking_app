package com.dev.james.domain.usecases

import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import javax.inject.Inject

class SetActiveBookUsecase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(bookId : String){
        dataStoreManager.storeStringValue(
            DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID ,
            bookId
        )
    }
}