package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class ReserveResponse (
        @SerializedName("Title")
        val title: String?,
        @SerializedName("CopyNumber")
        val copyNumber: String?,
        @SerializedName("CreateDate")
        val createDate: String?,
        @SerializedName("ExpiredDate")
        val expiredDate: String?
)