package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoPaquetes;

import java.util.List;


@Dao
public abstract class PaqueteDao {


    @Query("select count(*) from tb_paquetes  LIMIT 1")
    public abstract boolean existePaquetes();

    @Query("select * from tb_paquetes where idSistema=:id limit 1")
    public abstract PaqueteEntity fechConsultaPaqueteEspecifico(Integer id);

    @Query("select * from tb_paquetes")
    public abstract PaqueteEntity fechConsultaPaquete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createPaquete(PaqueteEntity entity);

    public void saveOrUpdate(DtoPaquetes paquetes ){

            PaqueteEntity paquete = fechConsultaPaquete();
            if(paquete==null){
                paquete = new PaqueteEntity();
                paquete.setIdSistema(paquete.getIdSistema());
                paquete.setIndex(paquete.getIndex());
                paquete.setDescripcion(paquetes.getDescripcion());
                paquete.setFunda(paquetes.getFunda());
                paquete.setGuardian(paquetes.getGuardian());
                paquete.setFlagAdicionales(paquetes.getFlagAdicionales());
                paquete.setFlagAdicionalGuardian(paquete.getFlagAdicionalGuardian());
                paquete.setFlagAdicionalFunda(paquetes.getFlagAdicionalFunda());
                paquete.setPaquetePorRecolccion(paquetes.getPaquetePorRecolccion());

            }else{
                paquete.setDescripcion(paquetes.getDescripcion());
                paquete.setFunda(paquetes.getFunda());
                paquete.setGuardian(paquetes.getGuardian());
                paquete.setFlagAdicionales(paquetes.getFlagAdicionales());
                paquete.setFlagAdicionalGuardian(paquete.getFlagAdicionalGuardian());
                paquete.setFlagAdicionalFunda(paquetes.getFlagAdicionalFunda());
                paquete.setPaquetePorRecolccion(paquetes.getPaquetePorRecolccion());
            }
            createPaquete(paquete);

    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoEntity entity);

    public void saveOrUpdate2(DtoPaquetes paquetes){
        PaqueteEntity entity;
        entity = fechConsultaPaqueteEspecifico(paquetes.getIdSistema());
        if (entity==null){
        //entity = new PaqueteEntity();

        }else{

        }

    }
}
