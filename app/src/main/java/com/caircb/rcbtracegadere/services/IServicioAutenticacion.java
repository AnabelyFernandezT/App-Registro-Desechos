package com.caircb.rcbtracegadere.services;

import com.caircb.rcbtracegadere.models.request.RequestCredentials;
import com.caircb.rcbtracegadere.models.request.RequestTokenFCM;
import com.caircb.rcbtracegadere.models.response.DtoUserCredential;
import com.caircb.rcbtracegadere.models.response.DtoUserTokenCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IServicioAutenticacion {

    @POST("login/authorization")
    Call<DtoUserCredential> autentication(@Body RequestCredentials credentials);

    @PUT("login/registerTokenFCM")
    Call<DtoUserTokenCredentials> registrarSession(@Body RequestTokenFCM credentials);
}
