package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class NewEBookResponse(
        @SerializedName("Author")
        val author: String?,
        @SerializedName("Content")
        val content: String?,
        @SerializedName("CoverPicture")
        val coverPicture: String?,
        @SerializedName("DocType")
        val docType: Int?,
        @SerializedName("Href")
        val href: Int?,
        @SerializedName("Id")
        val id: Long?,
        @SerializedName("PublishedYear")
        val publishedYear: String?,
        @SerializedName("Publisher")
        val publisher: String?,
        @SerializedName("Title")
        val title: String?
)