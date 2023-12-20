package com.dev.james.domain.usecases

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.dev.james.booktracker.core.common_models.Book
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        PDFBoxResourceLoader.init(context)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend operator fun invoke() {
        try{
            getPdfInStorage { files ->
                Timber.tag(TAG).d("pdf list : $files")
                val processedFiles = files.map { file ->
                    val document = PDDocument.load(file)

                    if(document.isEncrypted){
                        Book()
                    }else{
                        val pageSize = document.numberOfPages
                        val pdfTitle = document.documentInformation.title
                        val pdfAuthor = document.documentInformation.author
                        val pdfId = document.documentId.toString()
                        Book(
                            bookId = pdfId ,
                            bookAuthors = listOf(pdfAuthor) ,
                            bookUri = file.toUri() ,
                            bookTitle = pdfTitle ,
                            bookPagesCount = pageSize
                        )
                    }

                }

                Timber.tag(TAG).d("pdf list : $processedFiles")

            }
        }catch (e : Exception){
            Timber.tag(TAG).d("error fetching pdf : ${e.localizedMessage}")
        }

    }

    private suspend fun getPdfInStorage( setPdfFiles : (files : List<File>) -> Unit){
        withContext(Dispatchers.IO){
            try {
                val uri = MediaStore.Files.getContentUri("external")
                val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
                val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                val selectionArgs = arrayOf("application/pdf")
                val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val pdfList = mutableListOf<File>()

                if(cursor != null){
                    val pdfPathIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                    while (cursor.moveToNext()) {
                        if (pdfPathIndex != -1) {
                            val pdfPath = cursor.getString(pdfPathIndex)
                            val pdfFile = File(pdfPath)
                            if (pdfFile.exists() && pdfFile.isFile) {
                                pdfList.add(pdfFile);
                            }
                        }
                    }
                    cursor.close()
                    setPdfFiles(pdfList)
                }else{
                    Timber.tag(TAG).d("Cursor returned null!")
                    setPdfFiles(emptyList<File>())
                }

            }catch (e : Exception){
                Timber.tag(TAG).e("Error => Exception: ${e.localizedMessage}")
                e.printStackTrace()
            }
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