package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaObservacionesEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoPlantaCodigoQR;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;

import java.util.List;

@Dao
public abstract class ManifiestoPlantaObservacionesDao {

    @Query("select count(*) from tb_manifiestos_planta ")
    public abstract int contarHojaRutaProcesada();

    @Query("select * from tb_manifiestos_planta_observaciones where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoPlantaObservacionesEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);


    @Query("Select * from  tb_manifiestos_planta_observaciones where idAppManifiesto=:idManifiesto")
    public abstract DtoManifiestoPlantaObservacion obtenerObservaciones(Integer idManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoPlantaObservacionesEntity entity);

    public void saveOrUpdate(DtoManifiestoPlantaObservacion manifiesto){

        ManifiestoPlantaObservacionesEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiesto());
        if(entity==null ){
            entity = new ManifiestoPlantaObservacionesEntity();
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setPesoRecolectado(manifiesto.getPesoRecolectado());
            entity.setPesoPlanta(manifiesto.getPesoPlanta());
            entity.setObservacionPeso(manifiesto.getObservacionPeso());
            entity.setObservacionOtra(manifiesto.getObservacionOtra());
        }
        else if(entity!=null  ){
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setPesoRecolectado(manifiesto.getPesoRecolectado());
            entity.setPesoPlanta(manifiesto.getPesoPlanta());
            entity.setObservacionPeso(manifiesto.getObservacionPeso());
            entity.setObservacionOtra(manifiesto.getObservacionOtra());
        }


        if (entity!=null) createManifiesto(entity);
    }



}
