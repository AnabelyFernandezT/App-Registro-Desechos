package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDao {

    @Query("select * from tb_manifiestos_sede where idManifiestoPadre=:idManifiesto limit 1")
    public abstract ManifiestoSedeEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select idManifiestoPadre,manifiestos ,nombreCliente from tb_manifiestos_sede " )
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeEntity entity);

    public void saveOrUpdate(DtoManifiestoSede manifiesto){

        ManifiestoSedeEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiestoPadre());
        if(entity==null ){
            entity = new ManifiestoSedeEntity();
            entity.setIdManifiestoPadre(manifiesto.getIdManifiestoPadre());
            entity.setManifiestos(manifiesto.getManifiestos());
            entity.setNombreCliente(manifiesto.getNombreCliente());
        }
        else if(entity!=null  ){
            entity = new ManifiestoSedeEntity();
            entity.setIdManifiestoPadre(manifiesto.getIdManifiestoPadre());
            entity.setManifiestos(manifiesto.getManifiestos());
            entity.setNombreCliente(manifiesto.getNombreCliente());
        }


        if (entity!=null) createManifiesto(entity);
    }



}
