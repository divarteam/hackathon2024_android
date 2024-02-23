package ru.divarteam.franimal.data.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.divarteam.franimal.data.network.request.RegViaMailRequest
import ru.divarteam.franimal.data.network.response.IsOkResponse
import ru.divarteam.franimal.data.network.response.MailExistsResponse
import ru.divarteam.franimal.data.network.response.UserResponse

interface FraminalAPIService {

    @GET("check_mail_exists")
    fun checkMailExists(
        @Query("mail") mail: String
    ): Single<Response<MailExistsResponse>>

    @GET("send_mail_reg_code")
    fun sendMailRegCode(
        @Query("mail") mail: String
    ): Single<Response<IsOkResponse>>

    @POST("reg_via_mail")
    fun regViaMail(
        @Body body: RegViaMailRequest
    ): Single<Response<UserResponse>>

    @GET("send_mail_auth_code")
    fun sendMailAuthCode(
        @Query("mail") mail: String
    ): Single<Response<IsOkResponse>>

    @GET("auth_via_mail")
    fun authViaMail(
        @Query("mail") mail: String,
        @Query("code") code: String
    ): Single<Response<UserResponse>>
}