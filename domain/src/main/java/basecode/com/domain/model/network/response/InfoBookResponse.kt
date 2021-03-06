package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

class InfoBookResponse (
        @SerializedName("ListPath") val lstPath: List<String?>?,
        @SerializedName("Title") val title: String?,
        @SerializedName("Publisher") val publisher: String?,
        @SerializedName("PublishYear") val publishYear: String?,
        @SerializedName("ShortDescription") val shortDescription: String?,
        @SerializedName("ListCopyNumber") val lstCopyNumber: List<String?>?,
        @SerializedName("ListFreeCopyNumber") val lstFreeCopyNumber: List<String?>?,
        @SerializedName("DDC") val info: String,
        @SerializedName("Href") val linkShare: String?,
        @SerializedName("CoverPicture") val coverPicture: String?,
        @SerializedName("Author") val author: String?
)