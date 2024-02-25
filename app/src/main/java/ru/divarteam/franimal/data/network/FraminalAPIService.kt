package ru.divarteam.franimal.data.network

import androidx.annotation.Keep
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.divarteam.franimal.data.network.request.RegViaMailRequest
import ru.divarteam.franimal.data.network.request.UpdateUserRequest
import ru.divarteam.franimal.data.network.response.CardResponse
import ru.divarteam.franimal.data.network.response.CommentResponse
import ru.divarteam.franimal.data.network.response.IsOkResponse
import ru.divarteam.franimal.data.network.response.MailExistsResponse
import ru.divarteam.franimal.data.network.response.NoteResponse
import ru.divarteam.franimal.data.network.response.ProductResponse
import ru.divarteam.franimal.data.network.response.UserResponse

@Keep
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

    @GET("auth_via_vk")
    fun authViaVk(
        @Query("vk_code") code: String
    ): Single<Response<UserResponse>>

    @GET("auth_via_tg")
    fun authViaTg(
        @Query("tg_code") code: String
    ): Single<Response<UserResponse>>

    @GET("get_me")
    fun getMe(
        @Header("Authorization") token: String
    ): Single<Response<UserResponse>>

    @GET("get_user")
    fun getUserById(
        @Header("Authorization") token: String,
        @Query("user_int_id") userIntId: Int
    ): Single<Response<UserResponse>>

    @POST("update_user")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body body: UpdateUserRequest
    ): Single<Response<UserResponse>>

    @Multipart
    @POST("update_user_photo")
    fun updateUserPhoto(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Single<Response<String>>

    @GET("get_products")
    fun getProducts(
        @Header("Authorization") token: String
    ): Single<Response<List<ProductResponse>>>

    @GET("buy_product")
    fun buyProduct(
        @Header("Authorization") token: String,
        @Query("product_int_id") productIntId: Int
    ): Single<Response<IsOkResponse>>

    @POST("create_note")
    fun createNote(
        @Header("Authorization") token: String,
        @Query("text") text: String
    ): Single<Response<NoteResponse>>

    @Multipart
    @POST("create_note")
    fun createNote(
        @Header("Authorization") token: String,
        @Query("text") text: String,
        @Part file: MultipartBody.Part
    ): Single<Response<NoteResponse>>

    @GET("get_notes")
    fun getNotes(
        @Header("Authorization") token: String
    ): Single<Response<List<NoteResponse>>>

    @GET("get_note")
    fun getNote(
        @Header("Authorization") token: String,
        @Query("note_int_id") noteIntId: Int
    ): Single<Response<NoteResponse>>

    @GET("like_note")
    fun likeNote(
        @Header("Authorization") token: String,
        @Query("note_int_id") noteIntId: Int
    ): Single<Response<IsOkResponse>>

    @GET("search_users_by_q")
    fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") queue: String
    ): Single<Response<List<UserResponse>>>

    @GET("search_cards_by_q")
    fun searchCardsByQueue(
        @Header("Authorization") token: String,
        @Query("q") queue: String
    ): Single<Response<List<CardResponse>>>

    @GET("create_comment")
    fun createComment(
        @Header("Authorization") token: String,
        @Query("text") text: String,
        @Query("note_int_id") noteIntId: Int
    ): Single<Response<CommentResponse>>

    @GET("rm_comment")
    fun removeComment(
        @Header("Authorization") token: String,
        @Query("note_int_id") noteIntId: Int
    ): Single<Response<IsOkResponse>>

    @GET("say_hello")
    fun sayHello(
        @Header("Authorization") token: String,
        @Query("target_user_int_id") targetUserIntId: Int
    ): Single<Response<IsOkResponse>>
}