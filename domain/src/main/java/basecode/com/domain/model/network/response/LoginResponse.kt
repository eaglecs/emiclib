package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("access_token")
        var accessToken: String?,
        @SerializedName("token_type")
        var tokenType: String?
)