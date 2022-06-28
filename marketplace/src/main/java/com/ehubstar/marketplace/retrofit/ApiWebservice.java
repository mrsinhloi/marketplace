package com.ehubstar.marketplace.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiWebservice {

    //APIS ----------------------------------------------------------------------------------------
    @POST("/mpimage/getAllPost")
    Call<JsonObject> getAllPost(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/addImageItem")
    Call<JsonObject> addImageItem(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getPostByDemand")
    Call<JsonObject> getPostByDemand(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/updateImageItem")
    Call<JsonObject> updateImageItem(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/get-list-by-group")
    Call<JsonObject> getListByGroup(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getAllPostByUser")
    Call<JsonObject> getAllPostByUser(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/report")
    Call<JsonObject> report(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/delete")
    Call<JsonObject> delete(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getMarketplaceHome")
    Call<JsonObject> getMarketplaceHome(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/updateGroupChatId")
    Call<JsonObject> updateGroupChatId(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/createWebLink")
    Call<JsonObject> createWebLink(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/deleteGroupChatId")
    Call<JsonObject> deleteGroupChatId(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getImageById")
    Call<JsonObject> getImageById(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getShareLink")
    Call<JsonObject> getShareLink(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/like")
    Call<JsonObject> like(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/unlike")
    Call<JsonObject> unlike(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/getLikeList")
    Call<JsonObject> getLikeList(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/comment/add")
    Call<JsonObject> add(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/comment/like")
    Call<JsonObject> commentLike(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/comment/unlike")
    Call<JsonObject> commentUnline(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/comment/delete")
    Call<JsonObject> commentDelete(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/comment/getLikeList")
    Call<JsonObject> commentGetLikeList(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpimage/commentOfPost")
    Call<JsonObject> commentOfPost(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpgroup/info")
    Call<JsonObject> groupInfo(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpgroup/list-group")
    Call<JsonObject> listGroup(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpgroup/notifySettings/set")
    Call<JsonObject> setNotifySetting(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpgroup/notifySettings/get")
    Call<JsonObject> getNotifySetting(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );

    @POST("/mpgroup/getAllGroupList")
    Call<JsonObject> getAllGroupList(
            @Header("Authorization") String Authorization,
            @Body JsonObject json
    );


    //END APIS---------------------------------------------------------------------------------------


}
