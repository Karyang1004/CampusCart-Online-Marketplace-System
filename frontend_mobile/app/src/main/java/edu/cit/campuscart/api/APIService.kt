package edu.cit.campuscart.api

import edu.cit.campuscart.models.GoogleLoginRequest
import edu.cit.campuscart.models.GoogleLoginResponse
import edu.cit.campuscart.models.LoginRequest
import edu.cit.campuscart.models.LoginResponse
import edu.cit.campuscart.models.Notification
import edu.cit.campuscart.models.Products
import edu.cit.campuscart.models.Seller
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface APIService {

    //login endpoint
    @Headers("Content-Type: application/json")
    @POST("api/user/postUserRecord")
    fun registerSeller(@Body seller: Seller): Call<Void>

    //register endpoint
    @Headers("Content-Type: application/json")
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

   // add product endpoint
   @Multipart
   @POST("/api/product/postproduct")
   fun postProduct(
       @Header("Authorization") token: String,
       @Part("name") name: RequestBody,
       @Part("pdtDescription") description: RequestBody,
       @Part("qtyInStock") quantity: RequestBody,
       @Part("buyPrice") price: RequestBody,
       @Part("category") category: RequestBody,
       @Part("status") status: RequestBody,
       @Part("conditionType") condition: RequestBody,
       @Part("user_username") username: RequestBody,
       @Part image: MultipartBody.Part // No name needed here
   ): Call<ResponseBody>

   // get all products that are not from user's endpoint
    @Headers("Content-Type: application/json")
    @GET("api/product/getAllProducts/{username}")
    fun getAllProducts(
        @Path("username") username: String
    ): Call<List<Products>>

    // Get product image
    @Headers("Content-Type: application/json")
    @GET("/{imagePath}")
    fun getProductImage(@Path("imagePath") imagePath: String?): Call<ResponseBody?>?

    @Headers("Content-Type: application/json")
    @GET("/uploads/{sellerPhoto}")
    fun getSellerPhoto(@Path("sellerPhoto") sellerPhoto: String?): Call<ResponseBody?>?

    @Headers("Content-Type: application/json")
    @GET("/approved")
    fun getApprovedProducts(): Call<List<Products>>

    @Headers("Content-Type: application/json")
    @GET("api/product/getProductsByUser/{username}")
    fun getProductsByUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<Products>>

    //Edit product endpoint
    @Multipart
    @PUT("api/product/putProductDetails/{code}")
    fun putProductDetails(
        @Header("Authorization") token: String,
        @Path("code") code: Int,
        @Part("product") product: RequestBody,
        @Part imagePath: MultipartBody.Part? = null
    ): Call<Products>

    //Delete product endpoint
    @DELETE("api/product/deleteProduct/{code}")
    fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("code") code: Int
    ): Call<Void>

    // Notification endpoint
    @GET("api/notifications/user/{username}")
    fun getUserNotifications(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<Notification>>

    @POST("/auth/google")
    fun googleLogin(@Body request: GoogleLoginRequest): Call<GoogleLoginResponse>
}