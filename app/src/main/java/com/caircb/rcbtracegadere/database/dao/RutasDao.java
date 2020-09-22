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

    @Query("select * from tb_rutas where codigo=:id")
    public  abstract RutasEntity fetchConsultarNombre(int id);

    @Query("select * from tb_rutas where codigo=:idSubRuta")
    public  abstract RutasEntity fetchConsultarRuta(int idSubRuta);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createRuta(RutasEntity entity);

    public void saveOrUpdate(DtoFindRutas catalogos){
        RutasEntity catalogo = fetchConsultarCatalogoEspecifico(catalogos.getIdSubRuta());

            if (catalogo == null) {
                catalogo = new RutasEntity(catalogos.getIdSubRuta(),catalogos.getNombreRuta(), catalogos.getIdInsumo(),catalogos.getTiposubruta(),catalogos.getFechaEntrega(),catalogos.getFechaLiquidacion(),catalogos.getFunda63(),catalogos.getFunda55(),catalogos.getPc1(),catalogos.getPc2(),catalogos.getPc4());
            } else {
                catalogo.setCodigo(catalogos.getIdSubRuta());
                catalogo.setNombre(catalogos.getNombreRuta());
                catalogo.setTiposubruta(catalogos.getTiposubruta());
                catalogo.setFechaEntrega(catalogos.getFechaEntrega());
                catalogo.setFechaLiquidacion(catalogos.getFechaLiquidacion());
                catalogo.setFunda55(catalogos.getFunda55());
                catalogo.setFunda63(catalogos.getFunda63());
                catalogo.setPc1(catalogos.getPc1());
                catalogo.setPc2(catalogos.getPc2());
                catalogo.setPc4(catalogos.getPc4());
                catalogo.setIdInsumo(catalogos.getIdInsumo());
            }

            createRuta(catalogo);

    }



}
