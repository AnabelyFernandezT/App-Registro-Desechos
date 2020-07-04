package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDetalleValorDao {

    @Query("select * from tb_manifiestos_sede_det_valor where idManifiestoDetalle=:idManifiesto limit 1")
    public abstract ManifiestoSedeDetalleValorEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeDetalleValorEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleValorSede manifiesto){

        ManifiestoSedeDetalleValorEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiestoDetalle());
        if(entity==null){
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(true);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValor());

        }else if(entity!=null  ){
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(true);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValor());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
