package basecode.com.domain.model.bus

class LoginSuccessEventBus(val type: Type, val avatar: String) {

    enum class Type(val value: Int) {
        HandleBook(1),
        UserInfo(2),
        BorrowBook(3),
        Notify(4),
        RenewBook(5),
        DownloadBook(6),
        ChangePass(7),
        RequestDocument(8),
        BookRoom(9),
        ScreenAccount(10),
        ScreenReturnBook(11),
        ScreenBorrowBook(12),
        ScreenSearchBooth(13),
        BorrowReturnBook(14),
        Normal(-1)
    }
}