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
        @SerializedName("Phone")
        val phone: String?,
        @SerializedName("Email")
        val email: String?,
        @SerializedName("link")
        val linkAvatar: String?,
        @SerializedName("PatronGroup")
        val patronGroup: String?
)