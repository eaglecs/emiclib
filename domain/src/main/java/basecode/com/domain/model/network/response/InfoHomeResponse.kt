package basecode.com.domain.model.network.response

class InfoHomeResponse(val lstNewNews: MutableList<NewNewsModel> = mutableListOf(),
                       val lstNewBook: MutableList<NewBooksModel> = mutableListOf(),
                       val lstNewEBook: MutableList<NewEBookModel> = mutableListOf(),
                       val lstCollectionRecommend: MutableList<CollectionRecommendModel> = mutableListOf())

class NewNewsModel(val id: Int, val picture: String)
class NewBooksModel(val id: Int, val title: String, val coverPicture: String, val author: String)
class NewEBookModel(val id: Int, val title: String, val coverPicture: String, val author: String)
class CollectionRecommendModel(val id: Int, val name: String, val coverPicture: String)