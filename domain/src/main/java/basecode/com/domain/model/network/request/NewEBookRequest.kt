package basecode.com.domain.model.network.request

import com.google.gson.annotations.SerializedName

class NewEBookRequest {
    data class ActivitiesRequest(
            @SerializedName("numberItem")
            var numberItem: Int,
            @SerializedName("pageIndex")
            var pageIndex: Int,
            @SerializedName("pageSize")
            var pageSize: Int
    )
}