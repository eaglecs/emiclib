package basecode.com.domain.model.network.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("username")
        var username: String,
        @SerializedName("password")
        var password: String,
        @SerializedName("grant_type")
        var grantType: String = "password"
)