package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName


data class InfoUserResponse(
        @SerializedName("PatronCode")
        val patronCode: String?,
        @SerializedName("ValidDate")
        val validDate: String?,
        @SerializedName("ExpiredDate")
        val expiredDate: String?,
        @SerializedName("PatronName")
        val patronName: String?,
        @SerializedName("PatronGroup")
        val patronGroup: String?
)