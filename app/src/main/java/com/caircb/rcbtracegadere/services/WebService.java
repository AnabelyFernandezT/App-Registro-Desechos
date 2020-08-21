package com.caircb.rcbtracegadere.services;


import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebService {
    private static IServicio singleton;
    private static IServicioAutenticacion singletonAutenticacion;

    public static synchronized IServicio api(){
        if (singleton == null) {

            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                    if(json == null) return null;
                    try {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }catch (Exception ex) {
                        try {

                            String fecha = json.getAsString();

                            if (fecha.length() == 19)
                                return df2.parse(fecha);
                            else
                                return df1.parse(fecha);

                        } catch (final java.text.ParseException e) {

                            return null;
                        }
                    }
                }


            });

            builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    formatter.setTimeZone(TimeZone.getDefault());
                    String dateFormatAsString = formatter.format(date);
                    return new JsonPrimitive(dateFormatAsString);
                }
            });

            Gson gson = builder.create();
            //OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            OkHttpClient.Builder httpClient = getNewHttpClient();
            System.out.println(MySession.getId());
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer "+ MySession.getId()).build());

                }
            });

            Retrofit mRestAdapter = new Retrofit.Builder()
                    .baseUrl(MyConstant.PATH)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            singleton = mRestAdapter.create(IServicio.class);

        }

        return singleton;
    }

    public static synchronized IServicioAutenticacion seg() {

        if (singletonAutenticacion == null) {

            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    try {

                        String fecha = json.getAsString();

                        if(fecha.length()==19)
                            return df2.parse(fecha);
                        else
                            return df1.parse(fecha);

                    } catch (final java.text.ParseException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            });

            Gson gson = builder.create();

            OkHttpClient.Builder httpClient = getNewHttpClient();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    //String a = MySession.getId();
                    //Request request = chain.request();
                            if(!chain.request().url().toString().contains("login/authorization")) {
                                return chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer "+ MySession.getId()).build());
                            }
                    return chain.proceed(chain.request());
                }
            });



            Retrofit mRestAdapter = new Retrofit.Builder()
                    .baseUrl(MyConstant.PATH_SAPI)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            singletonAutenticacion = mRestAdapter.create(IServicioAutenticacion.class);

        }

        return singletonAutenticacion;
    }


    private static OkHttpClient.Builder getNewHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        return  Https.enableTls12OnPreLollipop(client);
    }

}
