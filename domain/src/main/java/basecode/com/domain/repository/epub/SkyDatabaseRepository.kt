package basecode.com.domain.repository.epub

import com.skytree.epub.BookInformation
import java.io.File

interface SkyDatabaseRepository {
    fun getListBook(): List<BookInformation>
    fun getBookCodeByFileName(title: String): Int
    fun getFileNameByBookCode(bookCode: Int): String
    fun reloadBookInformations()
    fun updateDownloadProcess(bi: BookInformation)
    fun insertEmptyBook(url: String, coverUrl: String, title: String, author: String, downloadId: Long): Int
    fun updateBook(bi: BookInformation)
    fun getCoverPathByBookCode(bookCode: Int): String
    fun deleteRecursive(file: File)
    fun deleteBookByBookCode(bookCode: Int)
    fun deleteBookmarksByBookCode(bookCode: Int)
    fun deleteHighlightsByBookCode(bookCode: Int)
    fun deletePagingsByBookCode(bookCode: Int)
}