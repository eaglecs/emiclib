package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName


data class NewNewsResponse(
        @SerializedName("Details")
        val details: String?,
        @SerializedName("Id")
        val id: Int?,
        @SerializedName("Picture")
        val picture: String?,
        @SerializedName("Summary")
        val summary: String?,
        @SerializedName("Title")
        val title: String?
)