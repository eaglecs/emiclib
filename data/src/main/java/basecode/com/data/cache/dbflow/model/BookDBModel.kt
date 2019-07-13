package basecode.com.data.cache.dbflow.model

import basecode.com.data.cache.dbflow.AppDatabase
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = AppDatabase::class, allFields = true)
class BookDBModel(
        @PrimaryKey(autoincrement = true)
        var idAuto: Long = 0,
        var id: Long = 0,
        var title: String = "",
        var photo: String = "",
        var author: String = ""
) : BaseModel(), Serializable