package basecode.com.data.epub

import basecode.com.domain.repository.epub.SkyDatabaseRepository
import com.skytree.epub.BookInformation
import java.io.File

class SkyDatabaseImpl(private val sd: SkyDatabase) : SkyDatabaseRepository {
    override fun deleteRecursive(file: File) {
        sd.deleteRecursive(file)
    }

    override fun getCoverPathByBookCode(bookCode: Int): String {
        return sd.getCoverPathByBookCode(bookCode)
    }

    override fun updateBook(bi: BookInformation) {
        sd.updateBook(bi)
    }

    override fun insertEmptyBook(url: String, coverUrl: String, title: String, author: String, downloadId: Long): Int {
        return sd.insertEmptyBook(url, coverUrl, title, author, downloadId)
    }

    override fun updateDownloadProcess(bi: BookInformation) {
        sd.updateDownloadProcess(bi)
    }

    override fun reloadBookInformations() {
        sd.fetchBookInformations(SkyDatabase.sortType, "")
    }

    override fun getFileNameByBookCode(bookCode: Int): String = sd.getFileNameByBookCode(bookCode)

    override fun getBookCodeByFileName(title: String): Int = sd.getBookCodeByFileName(title)

    override fun getListBook(): List<BookInformation> {
        return sd.fetchBookInformations(SkyDatabase.sortType, "")
    }

}