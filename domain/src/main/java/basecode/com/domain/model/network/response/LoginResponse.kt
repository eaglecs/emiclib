package basecode.com.domain.model.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("response")
                         var response: String)