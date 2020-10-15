package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaDetalleEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;
import com.caircb.rcbtracegadere.models.response.DtoHojaRutaDetallePlantaLote;

import java.util.List;


@Dao
public abstract class RecepcionQrPlantaDetalleDao {

    @Query("select * from tb_recepcion_qr_detalle_planta")
    public abstract List<ItemQrDetallePlanta> fetchHojaRutaQrPlantaDetalle2();

    @Query("select * from tb_recepcion_qr_detalle_planta")
    public abstract RecepcionQrPlantaDetalleEntity fetchHojaRutaQrPlantaDetalle();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoDetalle(RecepcionQrPlantaDetalleEntity entity);

    @Query("delete from tb_recepcion_qr_detalle_planta")
    public abstract void deleteTable();

    public void saveOrUpdate(DtoHojaRutaDetallePlantaLote manifiesto, int idDetalle){
        RecepcionQrPlantaDetalleEntity entity;
        entity = fetchHojaRutaQrPlantaDetalle();
        if(entity==null){
            entity = new RecepcionQrPlantaDetalleEntity();
            entity.set_id(idDetalle);
            entity.setNombreDesecho(manifiesto.getNombreDesecho());
            entity.setPesoDesecho(manifiesto.getPesoDesecho().doubleValue());
        }else {
            entity.set_id(idDetalle);
            entity.setNombreDesecho(manifiesto.getNombreDesecho());
            entity.setPesoDesecho(manifiesto.getPesoDesecho().doubleValue());
        }
        createManifiestoDetalle(entity);
    }
}
