package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.models.response.DtoRecepcionQrPlanta;

@Dao
public abstract class RecepcionQrPlantaDao {

    @Query("select * from tb_recepcion_qr_planta")
    public abstract RecepcionQrPlantaEntity fetchHojaRutaQrPlanta();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(RecepcionQrPlantaEntity entity);

    public void saveOrUpdate(DtoRecepcionQrPlanta manifiesto) {
        RecepcionQrPlantaEntity entity;
        entity = fetchHojaRutaQrPlanta();
        if(entity==null){
            entity = new RecepcionQrPlantaEntity();
            entity.set_id(0);
            entity.setPesoTotalLote(manifiesto.getPesoTotalLote().doubleValue());
            entity.setCantidadTotalBultos(manifiesto.getCantidadTotalBultos());
            entity.setCantidadTotalManifiestos(manifiesto.getCantidadTotalManifiestos());
            entity.setNumerosManifiesto(manifiesto.getNumerosManifiesto());

        }else {
            entity.set_id(0);
            entity.setPesoTotalLote(manifiesto.getPesoTotalLote().doubleValue());
            entity.setCantidadTotalBultos(manifiesto.getCantidadTotalBultos());
            entity.setCantidadTotalManifiestos(manifiesto.getCantidadTotalManifiestos());
            entity.setNumerosManifiesto(manifiesto.getNumerosManifiesto());
        }
        createManifiesto(entity);
    }

}
