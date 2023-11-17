package com.dev.james.domain.usecases

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.Book
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FetchPdfBooks
@Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val TAG = "FetchPdfBooks"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    operator fun invoke() {
        Timber.tag(TAG).d("pdf list : ${getPDFUriList()}")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getPDFUriList(): List<String> {
        val pdfList = mutableListOf<String>()
        val externalStorageUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Downloads.MIME_TYPE + " = ?"
        val selectionArgs = arrayOf("application/pdf")
        val sortOrder = MediaStore.Downloads.DATE_ADDED + " DESC"

        val cursor = context.contentResolver.query(
            externalStorageUri,
            null,
            selection,
            selectionArgs,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val pdfFilePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA))
                pdfList.add(pdfFilePath)
            }

            cursor.close()
        }
        return pdfList
    }
}