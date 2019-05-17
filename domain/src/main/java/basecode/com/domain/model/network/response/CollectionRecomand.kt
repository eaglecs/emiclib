package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName


data class CollectionRecomand(
        @SerializedName("CoverPicture")
        val coverPicture: String?,
        @SerializedName("Description")
        val description: String?,
        @SerializedName("Id")
        val id: Int?,
        @SerializedName("Name")
        val name: String?
)