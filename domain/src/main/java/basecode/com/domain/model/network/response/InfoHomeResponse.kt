package basecode.com.domain.model.network.response

class InfoHomeResponse(val lstNewNews: MutableList<NewNewsModel> = mutableListOf(),
                       val lstNewBook: MutableList<NewBooksModel> = mutableListOf(),
                       val lstNewEBook: MutableList<NewEBookModel> = mutableListOf(),
                       val lstCollectionRecommend: MutableList<CollectionRecommendModel> = mutableListOf(),
                       val lstNewNewsBottom: MutableList<NewNewsModel> = mutableListOf())

class NewNewsModel(val id: Long, val picture: String, val title: String, val content: String, val summary: String)
class NewBooksModel(val id: Long, val title: String, val coverPicture: String, val author: String)
class NewEBookModel(val id: Long, val title: String, val coverPicture: String, val author: String)
class CollectionRecommendModel(val id: Int, val name: String, val coverPicture: String)