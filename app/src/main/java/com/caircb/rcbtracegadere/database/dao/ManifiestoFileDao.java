package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoFileEntity;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;

import java.util.List;

@Dao
public abstract class ManifiestoFileDao {

    public static final Integer FOTO_NOVEDAD_FRECUENTE=1;
    public static final Integer FOTO_NO_RECOLECCION=2;
    public static final Integer FOTO_NOVEDAD_FRECUENTE_RECEPCION=3;
    public static final Integer FOTO_FIRMA_TRANSPORTISTA=10;
    public static final Integer FOTO_FIRMA_TECNICO_GENERADOR=11;
    public static final Integer FOTO_FIRMA_OPERADOR1=12;
    public static final Integer FOTO_FIRMA_OPERADOR2=13;
    public static final Integer FOTO_FIRMA_RECEPCION_PLATA=12;
    public static final Integer AUDIO_RECOLECCION=20;



    @Query("select code,file as foto,0 as tipo, '' as fotoUrl from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo  and tipo=:tipo and status=:status")
    public abstract List<ItemFoto> obtenerFotografiabyManifiestoCatalogo(Integer idAppManifiesto, Integer idCatalogo, Integer tipo,Integer status);

    @Query("select count(*) from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo  and tipo=:tipo limit 1")
    public abstract int obtenerCantidadFotografiabyManifiestoCatalogo(Integer idAppManifiesto, Integer idCatalogo, Integer tipo);

    @Query("select * from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo and code=:code and status=:status and tipo=:tipo limit 1")
    abstract ManifiestoFileEntity obtenerFotografiaEspecifica(Integer idAppManifiesto, Integer idCatalogo, Integer code, Integer tipo, Integer status);

    @Query("select * from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto and status=:status and tipo=:tipo limit 1")
    abstract ManifiestoFileEntity obtenerFotografiaEspecifica(Integer idAppManifiesto, Integer tipo, Integer status);


    @Query("Select code, file as foto, tipo, fileUrl as fotoUrl from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto")
    public abstract List<ItemFoto> obtenerAllFotosByIdManifiesto(Integer idAppManifiesto);

    @Query("Select file,fileUrl as url from tb_manifiestos_file where _id=:id")
    public abstract ItemFile obtenerFotosById(long id);


    @Query("select code as codigo ,fileUrl as urlImagen from tb_manifiestos_file where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo and tipo=:tipo")
    public abstract List<RequestNovedadFoto> consultarFotografias(Integer idAppManifiesto,Integer idCatalogo,Integer tipo);

    @Query("select _id  from tb_manifiestos_file" +
            " where idAppManifiesto=:idAppManifiesto and tipo=:tipo and sincronizado=0")
    public abstract List<Long> consultarFotografiasUpload(Integer idAppManifiesto,Integer tipo);

    @Query("Delete from tb_manifiestos_file where idAppManifiesto =:idAppManifiesto and idCatalogo=:idCatalogo")
    public abstract void deleteFotoByIdAppManifistoCatalogo( Integer idAppManifiesto, Integer idCatalogo);

    @Query("update tb_manifiestos set tiempoAudio=:tiempo where idAppManifiesto=:idManifiesto")
    abstract void updateTiempoFiletoManifiesto(Integer idManifiesto,String tiempo);

    @Query("select file,fileUrl as url  from tb_manifiestos_file where idAppManifiesto=:idManifiesto and tipo=:tipo and status=:status limit 1")
    public abstract ItemFile consultarFile(Integer idManifiesto,Integer tipo,Integer status);

    @Query("select _id as id ,file,fileUrl as url,sincronizado  from tb_manifiestos_file where idAppManifiesto=:idManifiesto and tipo=:tipo and status=:status and sincronizado=0 limit 1")
    public abstract DtoFile consultarFiletoSend(Integer idManifiesto, Integer tipo, Integer status);

    @Query("select _id as id ,file,fileUrl as url,sincronizado  from tb_manifiestos_file where idAppManifiesto=:idManifiesto and tipo=:tipo and status=:status limit 1")
    public abstract DtoFile consultarFiletoSendDefauld(Integer idManifiesto, Integer tipo, Integer status);

    @Query("update tb_manifiestos_file set sincronizado=:sincronizado where _id=:id")
    public abstract void actualizarToSincronizado(Long id,Boolean sincronizado);

    @Query("Delete from tb_manifiestos_file where idAppManifiesto =:idAppManifiesto and idCatalogo=:idCatalogo")
    public abstract void deleteFotoByIdAppManifistoCatalogoRecepcion( Integer idAppManifiesto, Integer idCatalogo);

    @Delete
    abstract void deleteFile(ManifiestoFileEntity model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createFoto(ManifiestoFileEntity entity);

    public void saveOrUpdate(Integer idAppManifiesto, Integer idCatalogo, Integer code, Integer tipo, String src,Integer status){
        ManifiestoFileEntity foto = obtenerFotografiaEspecifica(idAppManifiesto,idCatalogo,code,tipo,status);
        if(foto==null){
            foto = new ManifiestoFileEntity();
            foto.setIdAppManifiesto(idAppManifiesto);
            foto.setIdCatalogo(idCatalogo);
            foto.setCode(code);
            foto.setTipo(tipo);
            foto.setFile(src);
            foto.setFileUrl(AppDatabase.getFieldName("F"+code));
            foto.setSincronizado(false);
            foto.setStatus(status);
        }else{
            foto.setFile(src);
            foto.setFileUrl(AppDatabase.getFieldName("F"+code));
            foto.setSincronizado(false);
        }
        createFoto(foto);
    }

    public void saveOrUpdate(Integer idAppManifiesto, Integer tipo, String src,Integer status){
        ManifiestoFileEntity foto = obtenerFotografiaEspecifica(idAppManifiesto,tipo,status);
        if(src==null && foto!=null){
               //deleteFile(foto);
        }else {
            if (foto == null) {
                foto = new ManifiestoFileEntity();
                foto.setIdAppManifiesto(idAppManifiesto);
                foto.setTipo(tipo);
                foto.setFile(src);
                foto.setFileUrl(AppDatabase.getFieldName(""));
                foto.setSincronizado(false);
                foto.setStatus(status);
            } else {
                foto.setFile(src);
                foto.setFileUrl(AppDatabase.getFieldName(""));
                foto.setSincronizado(false);
            }
        }
        createFoto(foto);
    }

    //metodo para subir archivos tipo audio...
    public void saveOrUpdate(Integer idAppManifiesto, Integer tipo,String name, String src,String tiempo,Integer status){
        ManifiestoFileEntity file = obtenerFotografiaEspecifica(idAppManifiesto,tipo,status);
        if(src==null && file!=null){
           // deleteFile(file);
            updateTiempoFiletoManifiesto(idAppManifiesto,"");
        }else {
            if (file == null) {
                file = new ManifiestoFileEntity();
                file.setIdAppManifiesto(idAppManifiesto);
                file.setTipo(tipo);
                file.setFile(src);
                file.setFileUrl(name);
                file.setSincronizado(false);
                file.setStatus(status);
            } else {
                file.setFile(src);
                file.setFileUrl(name);
                file.setSincronizado(false);
            }

            updateTiempoFiletoManifiesto(idAppManifiesto,tiempo);
            createFoto(file);
        }
    }


}
