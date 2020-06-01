package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;


@Dao
public abstract class PaqueteDao {


    @Query("select count(*) from tb_paquetes LIMIT 1")
    public abstract boolean existePaquetes();

    @Query("select * from tb_paquetes where idSistema=:id")
    public abstract PaqueteEntity fechConsultaPaqueteEspecifico(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createPaquete(PaqueteEntity entity);

    public void saveOrUpdate(Integer paqueteID, Integer index, String descripcion, String funda, String guardian,
                             int flagAdicionales, int flagAdicionalFunda, int flagAdicionalGuardian, int paquetePorRecolccion){
        PaqueteEntity paquete = fechConsultaPaqueteEspecifico(paqueteID);
        if(paquete==null){
            paquete = new PaqueteEntity();
            paquete.setIdSistema(paqueteID);
            paquete.setIndex(index);
            paquete.setDescripcion(descripcion);
            paquete.setFunda(funda);
            paquete.setGuardian(guardian);
            paquete.setFlagAdicionales(flagAdicionales);
            paquete.setFlagAdicionalFunda(flagAdicionalFunda);
            paquete.setFlagAdicionalGuardian(flagAdicionalGuardian);
            paquete.setPaquetePorRecolccion(paquetePorRecolccion);
        }else{
            paquete.setIndex(index);
            paquete.setDescripcion(descripcion);
            paquete.setFunda(funda);
            paquete.setGuardian(guardian);
            paquete.setFlagAdicionales(flagAdicionales);
            paquete.setFlagAdicionalFunda(flagAdicionalFunda);
            paquete.setFlagAdicionalGuardian(flagAdicionalGuardian);
            paquete.setPaquetePorRecolccion(paquetePorRecolccion);
        }

        createPaquete(paquete);
    }
}
