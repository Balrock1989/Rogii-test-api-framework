package api.data

import okhttp3.FormBody
import okhttp3.RequestBody

fun getAuthBody(login:String, password: String) : RequestBody {
    return FormBody.Builder()
            .add("email", login)
            .add("password", password)
            .build()
}