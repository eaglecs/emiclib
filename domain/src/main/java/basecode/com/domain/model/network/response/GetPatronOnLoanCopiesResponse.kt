package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


class GetPatronOnLoanCopiesResponse : ArrayList<GetPatronOnLoanCopiesResponseItem>()

data class GetPatronOnLoanCopiesResponseItem(
    @SerializedName("CheckInDate")
    val checkInDate: String?,
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