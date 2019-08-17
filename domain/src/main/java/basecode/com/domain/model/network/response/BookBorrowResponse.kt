package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


data class BookBorrowResponse(
    @SerializedName("CheckOutDate")
    val checkOutDate: String?,
    @SerializedName("CopyNumber")
    val copyNumber: String?,
    @SerializedName("DueDate")
    val dueDate: String?,
    @SerializedName("ImageCover")
    val imageCover: String?,
    @SerializedName("OverDueDate")
    val overDueDate: Int?,
    @SerializedName("Title")
    val title: String?
)