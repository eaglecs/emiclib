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
        @SerializedName("MobiePhone")
        val phone: String?,
        @SerializedName("Email")
        val email: String?,
        @SerializedName("Portrait")
        val linkAvatar: String?,
        @SerializedName("Facebook")
        val facebook: String?,
        @SerializedName("Faculty")
        val faculty: String?,
        @SerializedName("PatronGroup")
        val patronGroup: String?
)