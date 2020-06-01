package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoFotografiaEntity;
import com.caircb.rcbtracegadere.models.ItemFoto;

import java.util.List;

@Dao
public abstract class ManifiestoFotografiasDao {


    @Query("select code,foto from tb_manifiestos_novedad_foto where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo  and tipo=:tipo")
    public abstract List<ItemFoto> obtenerFotografiabyManifiestoCatalogo(Integer idAppManifiesto, Integer idCatalogo, Integer tipo);

    @Query("select count(*) from tb_manifiestos_novedad_foto where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo  and tipo=:tipo limit 1")
    public abstract int obtenerCantidadFotografiabyManifiestoCatalogo(Integer idAppManifiesto, Integer idCatalogo, Integer tipo);


    @Query("select * from tb_manifiestos_novedad_foto where idAppManifiesto=:idAppManifiesto and idCatalogo=:idCatalogo and code=:code and tipo=:tipo limit 1")
    abstract ManifiestoFotografiaEntity obtenerFotografiaEspecifica(Integer idAppManifiesto, Integer idCatalogo, Integer code, Integer tipo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createFoto(ManifiestoFotografiaEntity entity);

    public void saveOrUpdate(Integer idAppManifiesto, Integer idCatalogo, Integer code, Integer tipo, String src){
        ManifiestoFotografiaEntity foto = obtenerFotografiaEspecifica(idAppManifiesto,idCatalogo,code,tipo);
        if(foto==null){
            foto = new ManifiestoFotografiaEntity();
            foto.setIdAppManifiesto(idAppManifiesto);
            foto.setIdCatalogo(idCatalogo);
            foto.setCode(code);
            foto.setTipo(tipo);
            foto.setFoto(src);
            foto.setFotoUrl(AppDatabase.getFieldName("F"+code));
        }else{
            foto.setFoto(src);
            foto.setFotoUrl(AppDatabase.getFieldName("F"+code));
        }
        createFoto(foto);
    }

}
