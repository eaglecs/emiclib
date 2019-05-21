package basecode.com.domain.model.network.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("Content-Type")
        var numberItem: String = "application/x-www-form-urlencoded",
        @SerializedName("username")
        var username: String,
        @SerializedName("password")
        var password: String,
        @SerializedName("grant_type")
        var grantType: String = "password"
)