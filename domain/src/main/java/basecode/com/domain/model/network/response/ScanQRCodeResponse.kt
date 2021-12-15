package basecode.com.domain.model.network.response
import com.google.gson.annotations.SerializedName


data class ScanQRCodeResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("type")
    val docType: Int?
)