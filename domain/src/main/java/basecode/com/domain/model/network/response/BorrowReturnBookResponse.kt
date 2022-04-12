package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


class BorrowReturnBookResponse : ArrayList<BorrowBookResponseItem>()

data class BorrowBookResponseItem(
    @SerializedName("intError")
    val intError: Int?,
    @SerializedName("strTransIDs")
    val strTransIDs: String?
)