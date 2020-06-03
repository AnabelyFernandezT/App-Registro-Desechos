package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;


@Dao
public abstract class ManifiestoPaqueteDao {

    public final static String ARG_INFECCIOSO="INFECCIOSO";
    public final static String ARG_CORTOPUNZANTE="CORTOPUNZANTE";
    public final static String ARG_INFECCIOSO_CORTOPUNZANTE="INFECCIOSO Y CORTOPUNZANTE";

    @Query("select * from tb_manifiestos_paquete where idAppManifiesto =:manifiestoID and idPaquete=:paqueteID")
    public abstract ManifiestoPaquetesEntity fetchConsultarManifiestoPaquetebyId(Integer manifiestoID, Integer paqueteID);

    @Update
    abstract void actualiarPaquete(ManifiestoPaquetesEntity entity);

    public void quitarPaquete(Integer idAppManifiesto, Integer idAppPaquete,String descripcion){
        ManifiestoPaquetesEntity pkg = fetchConsultarManifiestoPaquetebyId(idAppManifiesto,idAppPaquete);
        if(pkg!=null){
            if(descripcion.equals(ARG_INFECCIOSO))pkg.setDatosFundas(pkg.getDatosFundas()-1);
            else if (descripcion.equals(ARG_CORTOPUNZANTE))pkg.setDatosGuardianes(pkg.getDatosGuardianes()-1);
            actualiarPaquete(pkg);
        }
        //return pkg;
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoPaquete(ManifiestoPaquetesEntity entity);

    public void saveOrUpdate(Integer idAppManifiesto, Integer idAppPaquete,String descripcion){
        ManifiestoPaquetesEntity pkg = fetchConsultarManifiestoPaquetebyId(idAppManifiesto,idAppPaquete);
        if(pkg==null){
            pkg = new ManifiestoPaquetesEntity();
            pkg.setIdAppManifiesto(idAppManifiesto);
            pkg.setIdPaquete(idAppPaquete);
            if(descripcion.equals(ARG_INFECCIOSO)){pkg.setDatosFundas(1);pkg.setDatosGuardianes(0);}
            else if (descripcion.equals(ARG_CORTOPUNZANTE)){pkg.setDatosGuardianes(1);pkg.setDatosFundas(0);}
            else if (descripcion.equals(ARG_INFECCIOSO_CORTOPUNZANTE)){pkg.setDatosGuardianes(1);pkg.setDatosFundas(1);}
            pkg.setAdFundas(0);
            pkg.setAdGuardianes(0);
            pkg.setPqh(0);
        }else{
            if(descripcion.equals(ARG_INFECCIOSO))pkg.setDatosFundas(pkg.getDatosFundas()+1);
            else if (descripcion.equals(ARG_CORTOPUNZANTE))pkg.setDatosGuardianes(pkg.getDatosGuardianes()+1);
            //else if (descripcion.equals(ARG_INFECCIOSO_CORTOPUNZANTE)){pkg.setDatosGuardianes(1);pkg.setDatosFundas(1);}
        }

        createManifiestoPaquete(pkg);
    }




}
