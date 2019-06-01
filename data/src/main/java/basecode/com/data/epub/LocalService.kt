package basecode.com.data.epub

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.Log
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.epub.SkySetting
import basecode.com.domain.repository.epub.SkyDatabaseRepository
import com.skytree.epub.*
import org.koin.android.ext.android.inject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class LocalService : Service() {
    private val mBinder = LocalBinder()
    private val skyService: SkyDatabaseRepository by inject()
    private val keyManager: SkyKeyManager by inject()

    // called after downloading is finished.
    private val completeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            val query = Query()
            query.setFilterById(downloadId)
            val c = downloadManager.query(query) ?: return
            if (c.moveToFirst()) {
                val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    val sourceString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI))
                    val localFile = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
                    val uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    val title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val bookCode = skyService.getBookCodeByFileName(title)

                    postDownload(bookCode, bytes_total, localFile)
                }
            }
        }
    }

    fun debug(msg: String) {
        Log.d("EPub", msg)
    }

    override fun onCreate() {
        super.onCreate()
        val completeFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(completeReceiver, completeFilter)
        checkDownloads()
    }

    fun checkDownloads() {
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook.get(i)
            if (!bi.isDownloaded) {
                this.deleteBookByBookCode(bi.bookCode)
                this.deleteFileByDownloadId(bi.res0.toLong())
            }
        }
        reloadBookInformations()
    }

    fun deleteFileFromDownloads(bookCode: Int): Boolean {
        var deleted = false
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = skyService.getFileNameByBookCode(bookCode)
        val file = File(path, fileName)
        if (file.exists()) {
            deleted = file.delete()
        }
        return deleted
    }

    fun deleteFileByDownloadId(downloadId: Long) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder : Binder() {
        val service: LocalService
            get() = this@LocalService
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(completeReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun getMessage(msg: String): String {
        return msg
    }


    fun isBookExist(url: String): Boolean {
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook[i]
            if (bi.url.equals(url, ignoreCase = true)) return true
            if (bi.url.contains(url)) return true
            if (url.contains(bi.url)) return true
        }
        return false
    }

    fun getBookCodeByURL(url: String): Int {
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook[i]
            if (bi.url.equals(url, ignoreCase = true)) return bi.bookCode
            if (bi.url.contains(url)) return bi.bookCode
            if (url.contains(bi.url)) return bi.bookCode
        }
        return -1
    }


    fun isBookDownloaded(bookCode: Int): Boolean {
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook[i]
            if (bi.bookCode == bookCode) {
                return bi.isDownloaded
            }
        }
        return false
    }

    fun reloadBookInformations() {
        skyService.reloadBookInformations()
        this.sendReload()
    }

    fun reloadBookInformation(bookCode: Int) {
        skyService.reloadBookInformations()
        this.sendReloadBook(bookCode)
    }


    fun sendProgress(bookCode: Int, bytes_downloaded: Int, bytes_total: Int, percent: Double) {
        val bi = BookInformation()
        bi.bookCode = bookCode
        bi.fileSize = bytes_total
        bi.downSize = bytes_downloaded
        skyService.updateDownloadProcess(bi)

        val PROGRESS_INTENT = "com.skytree.android.intent.action.PROGRESS"
        val intent = Intent(PROGRESS_INTENT)
        intent.putExtra("BOOKCODE", bookCode)
        intent.putExtra("BYTES_DONWLOADED", bytes_downloaded)
        intent.putExtra("BYTES_TOTAL", bytes_total)
        intent.putExtra("PERCENT", percent)

        //		debug("Sender   BookCode:"+bookCode+" "+percent);
        this.sendBroadcast(intent)
    }

    fun sendReload() {
        val RELOAD_INTENT = "com.skytree.android.intent.action.RELOAD"
        val intent = Intent(RELOAD_INTENT)
        this.sendBroadcast(intent)
    }

    fun sendReloadBook(bookCode: Int) {
        val RELOADBOOK_INTENT = "com.skytree.android.intent.action.RELOADBOOK"
        val intent = Intent(RELOADBOOK_INTENT)
        intent.putExtra("BOOKCODE", bookCode)

        this.sendBroadcast(intent)
    }

    internal fun fileNameEncode(str: String): String {
        return str.replace(" ", "%20")
    }

    fun startDownload(url: String, coverUrl: String?, title: String, author: String) {
        var bookCode = -1
        try {
            bookCode = getBookCodeByURL(url)
            if (bookCode != -1) {
                if (this.isBookDownloaded(bookCode)) {
                    return
                }
            }
            bookCode = skyService.insertEmptyBook(url, coverUrl.valueOrEmpty(), title, author, 0)
            val targetName = skyService.getFileNameByBookCode(bookCode)
            val targetCover = targetName.replace(".epub", ".jpg")
            if (!(coverUrl == null || coverUrl.isEmpty())) donwloadCover(coverUrl, targetCover)
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request: DownloadManager.Request
            val urlToDownload = Uri.parse(this.fileNameEncode(url))
            request = DownloadManager.Request(urlToDownload)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, targetName)
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs()
            val downloadId = downloadManager.enqueue(request)
            request.setTitle(targetName)
            val downloadTimer = Timer()
            downloadTimer.schedule(DownloadTask(bookCode, downloadId), 0, 100)
            reloadBookInformations()
        } catch (e: Exception) {
            deleteBook(bookCode)
            e.printStackTrace()
        }

    }

    fun resumeDownload(bi: BookInformation) {
        val bookCode = bi.bookCode
        try {
            val url = bi.url
            val coverUrl = bi.coverUrl
            val targetName = skyService.getFileNameByBookCode(bookCode)
            val targetCover = targetName.replace(".epub", ".jpg")
            if (!(coverUrl == null || coverUrl.isEmpty())) donwloadCover(coverUrl, targetCover)
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request: DownloadManager.Request
            val urlToDownload = Uri.parse(this.fileNameEncode(url))
            request = DownloadManager.Request(urlToDownload)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, targetName)
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs()
            val downloadId = downloadManager.enqueue(request)
            request.setTitle(targetName)
            val downloadTimer = Timer()
            downloadTimer.schedule(DownloadTask(bookCode, downloadId), 0, 250)
            reloadBookInformations()
        } catch (e: Exception) {
            deleteBook(bookCode)
            e.printStackTrace()
        }

    }


    fun donwloadCover(url: String, fileName: String) {
        try {
            val targetFile = "${SkySetting.getStorageDirectory()}/books/$fileName"
            DownloadCoverTask().execute(this.fileNameEncode(url), targetFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inner class DownloadCoverTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String): Void? {
            try {
                val url = params[0]
                val fileName = params[1]
                val bmp = getBitmapFromURL(url)
                val out = FileOutputStream(fileName)
                bmp!!.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.close()
                bmp.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
                //				Log.e("donwloadCover error: ", e.getMessage().toString());
            }

            return null
        }
    }


    internal inner class DownloadTask(bookCode: Int, downloadId: Long) : TimerTask() {
        var bookCode = -1
        var downloadId: Long = -1

        var cancelHandler: Handler = object : Handler() {
            override fun handleMessage(m: Message) {
                deleteBook(bookCode)
                reloadBookInformations()
            }
        }

        private val progressHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val bookCode = msg.data.getInt("BOOKCODE")
                val bytes_downloaded = msg.data.getInt("BYTES_DOWNLOADED")
                val bytes_total = msg.data.getInt("BYTES_TOTAL")
                val percent = msg.data.getDouble("PERCENT")
                sendProgress(bookCode, bytes_downloaded, bytes_total, percent)
            }
        }

        init {
            this.bookCode = bookCode
            this.downloadId = downloadId
        }

        override fun run() {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val q = DownloadManager.Query()
            q.setFilterById(downloadId)
            val cursor = downloadManager.query(q)
            cursor.moveToFirst()
            val ret = checkStatus(cursor)
            if (ret == false) {
                cancel()
                val msg = Message()
                cancelHandler.sendMessage(msg)
            }
            val bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            debug("total $bytes_total")
            cursor.close()
            val dl_progress = bytes_downloaded.toDouble() / bytes_total.toDouble()

            if (bytes_total > 0) {
                val msg = Message()
                val b = Bundle()
                b.putInt("BOOKCODE", bookCode)
                b.putInt("BYTES_DOWNLOADED", bytes_downloaded)
                b.putInt("BYTES_TOTAL", bytes_total)
                b.putDouble("PERCENT", dl_progress)
                msg.data = b
                progressHandler.sendMessage(msg)
            }
            if (isBookDownloaded(bookCode)) {
                downloadManager.remove(downloadId)
                debug("download finished successfully for BookCode:$bookCode")
                cancel()
            }
        }
    }

    fun postDownload(bookCode: Int, fileSize: Int, sourceFile: String) {
        var bi = BookInformation()
        bi.bookCode = bookCode
        bi.fileSize = fileSize
        bi.downSize = fileSize
        bi.isDownloaded = false
        skyService.updateBook(bi)
        val destFolder = "${SkySetting.getStorageDirectory()}/books"
        val df = File(destFolder)
        df.mkdir()
        val fileName = skyService.getFileNameByBookCode(bookCode)
        val targetFile = destFolder + "/" + skyService.getFileNameByBookCode(bookCode)
        SkySetting.moveFile(sourceFile, targetFile)
        val coverPath = skyService.getCoverPathByBookCode(bookCode)
        val baseDirectory = SkySetting.getStorageDirectory() + "/books"
        sendProgress(bookCode, 0, 0, 0.9)

        bi = getBookInformation(fileName, baseDirectory, coverPath)

        bi.bookCode = bookCode
        bi.fileSize = -1
        bi.downSize = -1
        bi.isDownloaded = true
        val tbi = bi
        skyService.updateBook(bi)
        Handler().postDelayed({ reloadBookInformation(tbi.bookCode) }, 500)
    }


    fun getBookInformation(fileName: String, baseDirectory: String, coverPath: String): BookInformation {
        debug(fileName)

        var bi = BookInformation()
        // SkyProvider is the default epub file handler since 5.0.
        val skyProvider = SkyProvider()
        bi = BookInformation()
        bi.setFileName(fileName)
        bi.setBaseDirectory(baseDirectory)
        bi.setContentProvider(skyProvider)
        val coverFile = File(coverPath)
        if (!coverFile.exists()) bi.setCoverPath(coverPath)
        skyProvider.setBook(bi.book)
        skyProvider.setKeyListener(KeyDelegate())
        bi.makeInformation()
        return bi
    }


    internal inner class KeyDelegate : KeyListener {
        override fun getKeyForEncryptedData(uuidForContent: String, contentName: String, uuidForEpub: String): String {
            // TODO Auto-generated method stub
            return keyManager.getKey(uuidForContent, uuidForEpub)
        }

        override fun getBook(): Book? {
            // TODO Auto-generated method stub
            return null
        }
    }

    fun deleteBookByBookCode(bookCode: Int) {
        val targetName = skyService.getFileNameByBookCode(bookCode)
        var filePath = "${SkySetting.getStorageDirectory()}/books/$targetName"
        val targetDir = SkySetting.removeExtention(filePath)
        val coverPath = skyService.getCoverPathByBookCode(bookCode)
        coverPath.replace(".epub", ".jpg")
        skyService.deleteRecursive(File(targetDir))
        filePath = "${SkySetting.getStorageDirectory()}/downloads/$targetName"
        skyService.deleteRecursive(File(filePath))
        skyService.deleteRecursive(File(coverPath))
        skyService.deleteBookByBookCode(bookCode)
        skyService.deleteBookmarksByBookCode(bookCode)
        skyService.deleteHighlightsByBookCode(bookCode)
        skyService.deletePagingsByBookCode(bookCode)
    }

    fun deleteCachedByBookCode(bookCode: Int) {
        val prefix = String.format("sb%d", bookCode)
        val cacheFolder = SkySetting.getStorageDirectory() + "/caches"
        val directory = File(cacheFolder).listFiles()
        if (directory != null) {
            for (file in directory!!) {
                if (file.getName().startsWith(prefix)) {
                    file.delete()
                }
            }
        }
    }

    fun deleteBook(bookCode: Int) {
        this.deleteBookByBookCode(bookCode)
        this.reloadBookInformations()
    }

    fun deleteAllBooks() {
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook[i]
            this.deleteBookByBookCode(bi.bookCode)
        }
        this.reloadBookInformations()
    }

    private fun checkStatus(cursor: Cursor): Boolean {
        var ret = true
        //column for status
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)
        //column for reason code if the download failed or paused
        val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        val reason = cursor.getInt(columnReason)

        var statusText = ""
        var reasonText = ""

        when (status) {
            DownloadManager.STATUS_FAILED -> {
                ret = false
                statusText = "STATUS_FAILED"
                when (reason) {
                    DownloadManager.ERROR_CANNOT_RESUME -> reasonText = "ERROR_CANNOT_RESUME"
                    DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText = "ERROR_DEVICE_NOT_FOUND"
                    DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText = "ERROR_FILE_ALREADY_EXISTS"
                    DownloadManager.ERROR_FILE_ERROR -> reasonText = "ERROR_FILE_ERROR"
                    DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText = "ERROR_HTTP_DATA_ERROR"
                    DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText = "ERROR_INSUFFICIENT_SPACE"
                    DownloadManager.ERROR_TOO_MANY_REDIRECTS -> reasonText = "ERROR_TOO_MANY_REDIRECTS"
                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText = "ERROR_UNHANDLED_HTTP_CODE"
                    DownloadManager.ERROR_UNKNOWN -> reasonText = "ERROR_UNKNOWN"
                }
            }
            DownloadManager.STATUS_PAUSED -> {
                statusText = "STATUS_PAUSED"
                when (reason) {
                    DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText = "PAUSED_QUEUED_FOR_WIFI"
                    DownloadManager.PAUSED_UNKNOWN -> reasonText = "PAUSED_UNKNOWN"
                    DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText = "PAUSED_WAITING_FOR_NETWORK"
                    DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText = "PAUSED_WAITING_TO_RETRY"
                }
            }
            DownloadManager.STATUS_PENDING -> statusText = "STATUS_PENDING"
            DownloadManager.STATUS_RUNNING -> statusText = "STATUS_RUNNING"
            DownloadManager.STATUS_SUCCESSFUL -> statusText = "STATUS_SUCCESSFUL"
        }

        //		debug(statusText+"  "+reasonText);
        return ret
    }


    fun isBookDownloaded(url: String): Boolean {
        val bookCode = getBookCodeByURL(url)
        if (bookCode == -1) return false
        val listBook = skyService.getListBook()
        for (i in 0 until listBook.size) {
            val bi = listBook[i]
            if (bi.bookCode == bookCode) {
                return bi.isDownloaded
            }
        }
        return false
    }

    @Synchronized
    fun installBook(url: String) {
        debug("instalBook start")
        var bookCode = -1
        try {
            if (isBookDownloaded(url)) return
            val extension = SkySetting.getFileExtension(url)
            if (!extension.contains("epub")) return
            val pureName = SkySetting.getPureName(url)
            debug("instalBook starts real")
            bookCode = skyService.insertEmptyBook(url, "", "", "", 0)
            val targetName = skyService.getFileNameByBookCode(bookCode)
            copyBookToDevice(url, targetName)

            val bi: BookInformation
            val coverPath = skyService.getCoverPathByBookCode(bookCode)
            val baseDirectory = SkySetting.getStorageDirectory() + "/books"

            bi = getBookInformation(targetName, baseDirectory, coverPath)
            bi.bookCode = bookCode
            bi.title = pureName
            bi.fileSize = -1
            bi.downSize = -1
            bi.isDownloaded = true
            skyService.updateBook(bi)
            debug("instalBook ends")
            Handler().postDelayed({ reloadBookInformation(bi.bookCode) }, 500)
        } catch (e: Exception) {
            debug(e.message.valueOrEmpty())
        }

    }

    @Synchronized
    fun copyBookToDevice(filePath: String, targetName: String) {
        try {
            val localInputStream: InputStream = if (filePath.contains("asset")) {
                val fileName = SkySetting.getFileName(filePath)
                this.assets.open("books/$fileName")
            } else {
                FileInputStream(filePath)
            }


            val bookDir = SkySetting.getStorageDirectory() + "/books"
            val path = "$bookDir/$targetName"
            val localFileOutputStream = FileOutputStream(path)
            val arrayOfByte = ByteArray(1024)
            var offset = localInputStream.read(arrayOfByte)
            while (offset > 0) {
                localFileOutputStream.write(arrayOfByte, 0, offset)
                offset = localInputStream.read(arrayOfByte)
            }
            localFileOutputStream.flush()
            localFileOutputStream.close()
            localInputStream.close()
        } catch (localIOException: IOException) {
            localIOException.printStackTrace()
            return
        }

    }

    companion object {

        fun getBitmapFromURL(src: String): Bitmap? {
            try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
    }

}