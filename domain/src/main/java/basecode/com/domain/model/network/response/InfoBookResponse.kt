package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

class InfoBookResponse (
        @SerializedName("ListPath") val lstPath: List<String?>?
)