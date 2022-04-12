package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


class BooksDetailResponse : ArrayList<BooksDetailResponseItem>()

data class BooksDetailResponseItem(
    @SerializedName("CheckInDate")
    val checkInDate: Any?,
    @SerializedName("CheckOutDate")
    val checkOutDate: String?,
    @SerializedName("CopyNumber")
    val copyNumber: String?,
    @SerializedName("DueDate")
    val dueDate: Any?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("ImageCover")
    val imageCover: String?,
    @SerializedName("OverdueDays")
    val overdueDays: Any?,
    @SerializedName("Title")
    val title: String?
)