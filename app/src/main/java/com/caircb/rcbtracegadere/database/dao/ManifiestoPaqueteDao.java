package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPaquete;

import java.util.List;


@Dao
public abstract class ManifiestoPaqueteDao {

    public final static String ARG_INFECCIOSO="INFECCIOSO";
    public final static String ARG_CORTOPUNZANTE="CORTOPUNZANTE";
    public final static String ARG_INFECCIOSO_CORTOPUNZANTE="INFECCIOSO Y CORTOPUNZANTE";

    @Query("select * from tb_manifiestos_paquete where idAppManifiesto =:manifiestoID and idPaquete=:paqueteID")
    public abstract ManifiestoPaquetesEntity fetchConsultarManifiestoPaquetebyId(Integer manifiestoID, Integer paqueteID);

    @Query("select * from tb_manifiestos_paquete")
    public abstract ManifiestoPaquetesEntity fetchConsultarManifiesto();

    @Update
    public abstract void actualiarPaquete(ManifiestoPaquetesEntity entity);

    @Query("update tb_manifiestos_paquete set datosFundasPendientes=:datosFundasPendientes, datosFundasDiferencia =:datosfundasDiferencia " +
            " where idAppManifiesto=:idManifiesto and idPaquete =:idPaquete")
    abstract void updatePendientesFundas (Integer idPaquete, Integer datosFundasPendientes, Integer datosfundasDiferencia, Integer idManifiesto);

    @Query("update tb_manifiestos_paquete set datosGuardianesPendientes =:datosGuardianesPendientes, datosGuardianesDiferencia =:datosGuardianesDiferencia " +
            "where idAppManifiesto=:idManifiesto and  idPaquete =:idPaquete")
    abstract void updatePendientesGuardianes (Integer idPaquete,  Integer datosGuardianesPendientes, Integer datosGuardianesDiferencia, Integer idManifiesto);

    @Query("delete from tb_manifiestos_paquete")
    public abstract void deleteTablePaquetes();

    public void UpdatePaquetesPendientesFundas (Integer idPaquete, Integer datosFundasPendientes, Integer datosfundasDiferencia, Integer idManifiesto){
        updatePendientesFundas(idPaquete,datosFundasPendientes, datosfundasDiferencia, idManifiesto);
    }

    public void UpdatePaquetesPendientesGuardianes(Integer idPaquete, Integer datosGuardianesPendientes, Integer datosGuardianesDiferencia, Integer idManifiesto){
        updatePendientesGuardianes(idPaquete, datosGuardianesPendientes, datosGuardianesDiferencia, idManifiesto);
    }

    //Validacion igualar a 1

    @Query("update tb_manifiestos_paquete set datosFundas=:cantidad where idAppManifiesto =:manifiestoID and idPaquete=:paqueteID")
    public abstract void updateDatoFundas(Integer manifiestoID, Integer paqueteID, Integer cantidad);

    @Query("update tb_manifiestos_paquete set datosGuardianes=:cantidad where idAppManifiesto =:manifiestoID and idPaquete=:paqueteID")
    public abstract void updateDatoGuardianes(Integer manifiestoID, Integer paqueteID, Integer cantidad);

    public void quitarPaquete(Integer idAppManifiesto, Integer idAppPaquete,String descripcion){
        ManifiestoPaquetesEntity pkg = fetchConsultarManifiestoPaquetebyId(idAppManifiesto,idAppPaquete);
        if(pkg!=null){
            if(descripcion.equals(ARG_INFECCIOSO) && pkg.getDatosFundas()>0)pkg.setDatosFundas(pkg.getDatosFundas()-1);
            else if (descripcion.equals(ARG_CORTOPUNZANTE) && pkg.getDatosGuardianes()>0)pkg.setDatosGuardianes(pkg.getDatosGuardianes()-1);
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
            //else if (descripcion.equals(ARG_INFECCIOSO_CORTOPUNZANTE)){pkg.setDatosGuardianes(1);pkg.setDatosFundas(1);}
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
