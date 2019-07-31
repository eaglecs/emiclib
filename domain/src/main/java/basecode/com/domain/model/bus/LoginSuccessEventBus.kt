package basecode.com.domain.model.bus

class LoginSuccessEventBus(val type: Type) {

    enum class Type {
        HandleBook,UserInfo, BorrowBook, Notify, RenewBook, DownloadBook, ChangePass, Normal
    }
}