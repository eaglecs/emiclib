package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName


data class NewNewsResponse(
        @SerializedName("Details")
        val details: String?,
        @SerializedName("Id")
        val id: Long?,
        @SerializedName("CopyNumber")
        val copyNumber: String?,
        @SerializedName("Picture")
        val picture: String?,
        @SerializedName("Summary")
        val summary: String?,
        @SerializedName("Title")
        val title: String?,
        @SerializedName("CoverPicture")
        val coverPicture: String?,
        @SerializedName("ImageCover")
        val imageCover: String?,
        @SerializedName("PublishedYear")
        val publishedYear: String?,
        @SerializedName("Publisher")
        val publisher: String?,
        @SerializedName("CheckOutDate")
        val checkOutDate: String?,
        @SerializedName("DueDate")
        val dueDate: String?,
        @SerializedName("Author")
        val author: String?
)