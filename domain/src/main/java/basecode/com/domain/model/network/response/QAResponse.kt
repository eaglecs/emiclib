package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class QAResponse(
        @SerializedName("Id")
        val id: Long?,
        @SerializedName("Question")
        val question: String?,
        @SerializedName("Answer")
        val answer: String?
)