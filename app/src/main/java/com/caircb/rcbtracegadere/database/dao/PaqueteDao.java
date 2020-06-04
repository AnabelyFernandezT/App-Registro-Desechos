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

    @Query("select * from tb_paquetes where idPAquete=:idPaquete limit 1")
    public abstract PaqueteEntity fechConsultaPaqueteEspecifico(Integer idPaquete);

    @Query("select paquetePorRecolccion from tb_paquetes where idPAquete=:idPaquete limit 1")
    public abstract Integer fechConsultaPaqueteRecoleccionPaqueteEspecifico(Integer idPaquete);

    @Query("select * from tb_paquetes")
    public abstract PaqueteEntity fechConsultaPaquete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createPaquete(PaqueteEntity entity);

    public void saveOrUpdate(DtoPaquetes model ){

            PaqueteEntity paquete = fechConsultaPaqueteEspecifico(model.getIdPaquete());
            if(paquete==null){
                paquete = new PaqueteEntity();
                paquete.setIdPAquete(model.getIdPaquete());
                paquete.setIndex(model.getI());
                paquete.setDescripcion(model.getNombrePaquete());
                paquete.setFunda(model.getTamanoFunda());
                paquete.setGuardian(model.getTamanoGuardian());
                paquete.setFlagAdicionales(model.getFlagAdicionales());
                paquete.setFlagAdicionalGuardian(model.getFlagAdicionalesGuardian());
                paquete.setFlagAdicionalFunda(model.getFlagAdicionalesFundas());
                paquete.setPaquetePorRecolccion(model.getPaquetePorRecoleccion());
                paquete.setEntregaFundas(model.getEntregaFundas()==1);
                paquete.setEntregaGuardianes(model.getEntregaGuardianes()==1);

            }else{
                paquete.setIndex(model.getI());
                paquete.setDescripcion(model.getNombrePaquete());
                paquete.setFunda(model.getTamanoFunda());
                paquete.setGuardian(model.getTamanoGuardian());
                paquete.setFlagAdicionales(model.getFlagAdicionales());
                paquete.setFlagAdicionalGuardian(model.getFlagAdicionalesGuardian());
                paquete.setFlagAdicionalFunda(model.getFlagAdicionalesFundas());
                paquete.setPaquetePorRecolccion(model.getPaquetePorRecoleccion());
                paquete.setEntregaFundas(model.getEntregaFundas()==1);
                paquete.setEntregaGuardianes(model.getEntregaGuardianes()==1);
            }
            createPaquete(paquete);

    }


}
