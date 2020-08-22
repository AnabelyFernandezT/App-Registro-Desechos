package com.caircb.rcbtracegadere.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.models.response.DtoListaLugar;
import com.caircb.rcbtracegadere.models.response.DtoListaMenu;
import com.google.gson.Gson;

import java.util.List;

public final class MySession {

    static SharedPreferences pSharedPref = MyApp.getsInstance().getSharedPreferences(MyConstant.SEG_SP, Context.MODE_PRIVATE);

    final static String LOGIN="islogin";
    final static String DEVICE="device";
    final static String CHIP="chip";
    final static String ID="id";
    final static String EMPRESA="empresa";
    final static String LUGAR="lugar";
    final static String LUGAR_NOMBRE="lugarNombre";
    final static String PROCESO="proceso";
    final static String PROCESO_NOMBRE="procesoNombre";
    final static String TOKEN="token";
    final static String TOKEN_USUARIO="tokenUsuario";
    final static String USUARIO="usuario";
    final static String USUARIO_NOMBRE="usuarioNombre";
    final static String APLICACION="aplicacion";
    final static String PERFIL="perfil";
    final static String MENUS = "lstMenus";
    final static String CONNECTICITY = "connecticity";
    final static String DISPOSITIVO_MANUFACTURER = "manufacturer";

    final static String DESTROY="isDestroy";
    final static String STOP="isStop";
    final static String ID_SUBRUTA = "idSubRuta";
    final static String DESTINO_ESPECIFICO = "destinoEspecifico";

    public static boolean isLocalStorage(){
        return pSharedPref!=null;
    }


    private static void set(String key, String value){
        if(pSharedPref!=null) {
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, value).commit();
        }
    }

    private static String get(String key){
        if(pSharedPref!=null) {
            return pSharedPref.getString(key,"");
        }
        return null;
    }

    public static boolean isLogin(){
        String str = get(LOGIN);
        return str!=null? Boolean.parseBoolean(str):false;
    }

    public static void setLogin(boolean login){
        set(LOGIN,""+login);
    }

    public static boolean isConnecticity(){
        String str = get(CONNECTICITY);
        return str!=null? Boolean.parseBoolean(str):false;
    }

    public static void setConnecticity(boolean login){
        set(CONNECTICITY,""+login);
    }

    public static void setIdDevice(String device){
        set(DEVICE,device);
    }

    public static String getIdDevice(){
        String str = get(DEVICE);
        return str!=null?str:"";
    }

    public static void setIdChip(String chip){
        set(CHIP,chip);
    }
    public static String getIdChip(){
        String str = get(CHIP);
        return str!=null?str:"";
    }

    public static void setIdEmpresa(Integer id){
        set(EMPRESA,""+id);
    }
    public static Integer getIdEmpresa(){
        String str = get(EMPRESA);
        return str!=null? Integer.parseInt(str):-1;
    }

    public static void setIdLugar(Integer id){
        set(LUGAR,""+id);
    }
    public static Integer getIdLugar(){
        String str = get(LUGAR);
        return str!=null? Integer.parseInt(str):-1;
    }

    public static void setLugarNombre(String lugar){
        set(LUGAR_NOMBRE,lugar);
    }

    public static String getLugarNombre(){
        String str = get(LUGAR_NOMBRE);
        return str!=null?str:"";
    }

    public static void setIdProceso(Integer id){
        set(PROCESO,""+id);
    }
    public static Integer getIdProceso(){
        String str = get(PROCESO);
        return str!=null? Integer.parseInt(str):-1;
    }

    public static void setProcesoNombre(String proceso){
        set(PROCESO_NOMBRE,proceso);
    }
    public static String getProcesoNombre(){
        String str = get(PROCESO_NOMBRE);
        return str!=null?str:"";
    }

    public static void setToken(String token){
        set(TOKEN,token);
    }
    public static String getToken(){
        String str = get(TOKEN);
        return str!=null?str:"";
    }

    public static void setIdUsuarioToken(Integer id){
        set(TOKEN_USUARIO,""+id);
    }
    public static Integer getIdUsuarioToken(){
        String str = get(TOKEN_USUARIO);
        return str!=null? Integer.parseInt(str!=""?str:"-1"):-1;
    }

    public static void setIdUsuario(Integer id){
        set(USUARIO,""+id);
    }
    public static Integer getIdUsuario(){
        String str = get(USUARIO);
        return (str!=null && !str.isEmpty()) ? Integer.parseInt(str):-1;
    }

    public static void setIdAplicacion(Integer id){
        set(APLICACION,""+id);
    }
    public static Integer getIdAplicacion(){
        String str = get(APLICACION);
        return str!=null? Integer.parseInt(str):-1;
    }

    public static void setIdPerfil(Integer id){
        set(PERFIL,""+id);
    }
    public static Integer getIdPerfil(){
        String str = get(PERFIL);
        return str!=null? Integer.parseInt(str):-1;
    }

    public static void setLugares(List<DtoListaLugar> lugares){
        Gson gson = new Gson();
        set(LUGAR,gson.toJson(lugares));
    }

    public static String getLugares(){
        String str = get(LUGAR);
        return str!=null?str:"";
    }

    public static void setMenus(List<DtoListaMenu> menus){
        Gson gson = new Gson();
        set(MENUS,gson.toJson(menus));
    }
    public static String getMenus(){
        String str = get(MENUS);
        return str!=null?str:"";
    }

    public static void setUsuarioNombre(String nombre){
        set(USUARIO_NOMBRE,nombre);
    }
    public static String getUsuarioNombre(){
        String str = get(USUARIO_NOMBRE);
        return str!=null?str:"";
    }

    public static void setId(String id){
        set(ID,id);
    }
    public static String getId(){
        String str = get(ID);
        return str!=null?str:"";
    }

    public static boolean isDestroy(){
        String str = get(DESTROY);
        return str!=null? Boolean.parseBoolean(str):false;
    }

    public static void setDestroy(boolean destroy){
        set(DESTROY,""+destroy);
    }

    public static void setStop(boolean stop){
        set(STOP,""+stop);
    }


    public static void setIdSubruta(Integer id){
        set(ID_SUBRUTA,""+id);
    }
    public static Integer getIdSubRuta(){
        String str = get(ID_SUBRUTA);
        if (str!=null && (!str.isEmpty())){
            return Integer.parseInt(str);
        }else {
            return -1;
        }
    }

    public static void setDestinoEspecifico(String destinoEspecifico){
        set(DESTINO_ESPECIFICO,""+destinoEspecifico);
    }
    public static String getDestinoEspecifico(){
        String str = get(DESTINO_ESPECIFICO);
        return str!=null? str : "";
    }

    public static String getDispositivoManufacturer(){
        String str = get(DISPOSITIVO_MANUFACTURER);
        return str!=null?str:"";
    }

    public static void setDispositivoManufacturer(String chip){
        set(DISPOSITIVO_MANUFACTURER,chip);
    }

}
