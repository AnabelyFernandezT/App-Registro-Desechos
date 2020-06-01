package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.models.response.DtoPaquetes;

import java.util.List;


@Dao
public abstract class PaqueteDao {


    @Query("select count(*) from tb_paquetes LIMIT 1")
    public abstract boolean existePaquetes();

    @Query("select * from tb_paquetes where idSistema=:id")
    public abstract PaqueteEntity fechConsultaPaqueteEspecifico(Integer id);

    @Query("select * from tb_paquetes")
    public abstract PaqueteEntity fechConsultaPaquete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createPaquete(PaqueteEntity entity);

    public void saveOrUpdate(List<DtoPaquetes> paquetes ){

        for (DtoPaquetes p:paquetes) {
            PaqueteEntity paquete = fechConsultaPaquete();
            if(paquete==null){
                paquete = new PaqueteEntity(p.getIdSistema(),p.getIndex(),p.getDescripcion(),p.getFunda(),p.getGuardian(),
                        p.getFlagAdicionales(),p.getFlagAdicionalFunda(),p.getFlagAdicionalGuardian(),p.getPaquetePorRecolccion());
            }else{
                paquete.setDescripcion(p.getDescripcion());
                paquete.setFunda(p.getFunda());
                paquete.setGuardian(p.getGuardian());
                paquete.setFlagAdicionales(p.getFlagAdicionales());
                paquete.setFlagAdicionalFunda(p.getFlagAdicionalFunda());
                paquete.setPaquetePorRecolccion(p.getPaquetePorRecolccion());
            }
            createPaquete(paquete);
        }

    }
}
