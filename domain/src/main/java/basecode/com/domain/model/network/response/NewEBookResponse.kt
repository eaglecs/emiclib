package basecode.com.domain.model.network.response

data class NewEBookResponse(
    val Author: String?,
    val Content: String?,
    val CoverPicture: String?,
    val DocType: Int?,
    val Href: Int?,
    val Id: Int?,
    val PublishedYear: String?,
    val Publisher: String?,
    val Title: String?
)