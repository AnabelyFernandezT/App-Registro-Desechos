package com.caircb.rcbtracegadere.database.dao;

import android.inputmethodservice.Keyboard;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.models.RowRutas;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;

import java.util.List;

@Dao
public abstract class RutasDao {


    @Query("select * from tb_rutas where codigo=:tipo")
    public abstract RutasEntity fetchConsultarCatalogoEspecifico(Integer tipo);

    @Query("select count(*) from tb_rutas  LIMIT 1")
    public abstract boolean existeRutas();

    @Query("select nombre as nombreRuta from tb_rutas")
    public abstract List<RowRutas> fetchConsultarRutas();

    @Query("select * from tb_rutas where nombre=:nombre")
    public  abstract RutasEntity fetchConsultarId(String nombre);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createRuta(RutasEntity entity);

    public void saveOrUpdate(DtoFindRutas catalogos){
        RutasEntity catalogo = fetchConsultarCatalogoEspecifico(catalogos.getIdSubRuta());

            if (catalogo == null) {
                catalogo = new RutasEntity(catalogos.getIdSubRuta(),catalogos.getNombreRuta());
            } else {
                catalogo.setCodigo(catalogos.getIdSubRuta());
                catalogo.setNombre(catalogos.getNombreRuta());

            }

            createRuta(catalogo);

    }



}
