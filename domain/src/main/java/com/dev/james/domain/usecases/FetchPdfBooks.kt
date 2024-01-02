package com.dev.james.domain.usecases

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.dev.james.booktracker.core.common_models.Book
import com.itextpdf.text.exceptions.BadPasswordException
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.ReaderProperties
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.InvalidPasswordException
import com.tom_roush.pdfbox.pdmodel.encryption.StandardDecryptionMaterial
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FetchPdfBooks
@Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val TAG = "FetchPdfBooks"
        const val USER_PASSWORD = " "
    }

    init {
        PDFBoxResourceLoader.init(context)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend operator fun invoke() {
        try {
            getPdfInStorage { files ->
                Timber.tag(TAG).d("pdf list : $files")
               // Timber.tag(TAG).d("file at pos 19 ${files[19]}")
                val processedFiles = files.map { file ->
                    extractPdfMetadata(file.toUri())
                }.filter { book ->
                    book.bookPagesCount!! > 20
                }

                Timber.tag(TAG).d("pdf list : $processedFiles")

            }
        } catch (e: InvalidPasswordException) {
            Timber.tag(TAG).d("error fetching pdf : ${e.localizedMessage}")
        }

    }

    private fun extractPdfMetadata(pdfUri : Uri) : Book {
        return try {
            val inputStream = context.contentResolver.openInputStream(pdfUri)
            val pdfReader = PdfReader(inputStream)
            val pdfInfo = pdfReader.info
            val pagesCount = pdfReader.numberOfPages
            val author = pdfInfo["Author"] ?: pdfInfo["Creator"] ?: "No author information"
            val publisher = pdfInfo["Producer"] ?: "No publisher information"
            val title = pdfInfo["Title"] ?: "No title"

            pdfReader.close()
            Book(
                bookPagesCount = pagesCount ,
                bookAuthors = listOf(author) ,
                bookUri = pdfUri ,
                publisher = publisher ,
                bookTitle = title
            )
        }catch (e : BadPasswordException ){
            Timber.tag(TAG).d("Bad password exception! => ${e.localizedMessage}")
            Book(
                bookPagesCount = 0
            )
        }catch (e : IOException){
            Timber.tag(TAG).d("IOException! => ${e.localizedMessage}")
            Book(
                bookPagesCount = 0
            )
        }

    }

    private suspend fun getPdfInStorage(setPdfFiles: suspend (files: List<File>) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val uri = MediaStore.Files.getContentUri("external")
                val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
                val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                val selectionArgs = arrayOf("application/pdf")
                val cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val pdfList = mutableListOf<File>()

                if (cursor != null) {
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
                } else {
                    Timber.tag(TAG).d("Cursor returned null!")
                    setPdfFiles(emptyList<File>())
                }

            } catch (e: Exception) {
                Timber.tag(TAG).e("Error => Exception: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }

    }

}