package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
        @SerializedName("Id")
        val id: Long?,
        @SerializedName("Title")
        val title: String?,
        @SerializedName("Message")
        val message: String?,
        @SerializedName("CreateDate")
        val createDate: String?,
        @SerializedName("Status")
        val status: Int?)