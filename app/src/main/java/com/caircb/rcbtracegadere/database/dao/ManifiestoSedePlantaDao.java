package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoSedePlantaEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoPendiente;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPendienteSede;

import java.util.List;


@Dao
public abstract class ManifiestoSedePlantaDao {

    @Query("select * from tb_manifiesto_sede_planta where idManifiesto=:idManifiesto")
    public abstract ManifiestoSedePlantaEntity fetchManifiestoSedePlanta(Integer idManifiesto);

    @Query("Select idManifiesto, numeroManifiesto, totBultos,totPendientes, estadoCheck, destinatarioAlterno from tb_manifiesto_sede_planta where idManifiestoPadre=:idManifiestoP ")
    public abstract List<ItemManifiestoPendiente> fetchManifiestoPendiente (Integer idManifiestoP);

    @Query("Select * from tb_manifiesto_sede_planta where idManifiestoPadre=:idManifiestoP and estadoCheck = 1")
    public abstract List<ItemManifiestoPendiente> fetchManifiestoPendienteCheck (Integer idManifiestoP);

    @Query("update tb_manifiesto_sede_planta set estadoCheck=:check where idManifiesto=:idManifiesto")
    public abstract void updateCheck (Integer idManifiesto, boolean check);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoPendiente(ManifiestoSedePlantaEntity entity);

    public void saveOrUpdate(Integer idManifiestoPadre, DtoManifiestoPendienteSede manifiesto){

        ManifiestoSedePlantaEntity entity;

        entity = fetchManifiestoSedePlanta(manifiesto.getIdManifiesto());

        if(entity == null){
            entity = new ManifiestoSedePlantaEntity();
            entity.setIdManifiestoPadre(idManifiestoPadre);
            entity.setIdManifiesto(manifiesto.getIdManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setTotBultos(manifiesto.getTotBultos());
            entity.setTotPendientes(manifiesto.getTotPendientes());
            entity.setEstadoCheck(false);
            entity.setDestinatarioAlterno(manifiesto.getDestinatarioAlterno());
        }else{
            entity.setIdManifiestoPadre(idManifiestoPadre);
            entity.setIdManifiesto(manifiesto.getIdManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setTotBultos(manifiesto.getTotBultos());
            entity.setTotPendientes(manifiesto.getTotPendientes());

        }
        createManifiestoPendiente(entity);
    }
}
