package com.dev.james.domain.usecases

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.Book
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
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
        getPdfInStorage { files ->
            Timber.tag(TAG).d("pdf list : $files")
        }
    }

    private fun getPdfInStorage( setPdfFiles : (files : List<File>) -> Unit){
        return  try {
            val folder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            )

            if(!folder.exists() || !folder.isDirectory){
                Timber.tag(TAG).e("Directory does not exist or is not a directory")
                return
            }
            val files = folder.listFiles{ _ , name ->
                name.endsWith(".pdf")
            }
            if(files != null){
                Timber.tag(TAG).d( message = "Files ${files.contentToString()}")
                setPdfFiles(files.toList())
            } else {
                Timber.tag(TAG).e("Error: No pdfs found in the directory")
            }
        }catch (e : Exception){
            Timber.tag(TAG).e("Error => Exception: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }

   /* @RequiresApi(Build.VERSION_CODES.Q)
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
    }*/
}