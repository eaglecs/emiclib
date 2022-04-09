package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


class BoothsResponse : ArrayList<BoothsResponseItem>()

data class BoothsResponseItem(
    @SerializedName("bLatitude")
    val bLatitude: Double?,
    @SerializedName("bLongitude")
    val bLongitude: Double?,
    @SerializedName("CodeLoc")
    val codeLoc: String?,
    @SerializedName("ID")
    val id: Long?,
    @SerializedName("IsBooth")
    val isBooth: Boolean?,
    @SerializedName("LibID")
    val libId: Long?,
    @SerializedName("MaxNumber")
    val maxNumber: Int?,
    @SerializedName("Status")
    val status: Boolean?,
    @SerializedName("Symbol")
    val symbol: String?
)