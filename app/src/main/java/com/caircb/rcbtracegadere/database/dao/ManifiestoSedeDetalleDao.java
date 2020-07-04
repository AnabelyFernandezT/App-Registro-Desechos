package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDetalleDao {
    @Query("delete from tb_manifiestos_sede_detalle")
    @Transaction
    public abstract void eliminarDetalle();

    @Query("select idManifiestoDetalle,codigoMae,codigo,nombreDesecho,totalBultos from tb_manifiestos_sede_detalle where idAppManifiesto=:idManifiesto" )
    @Transaction
    public abstract List<ItemManifiestoDetalleSede> fetchManifiestosAsigByClienteOrNumManif(Integer idManifiesto);

    @Query("select * from tb_manifiestos_sede_detalle where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoSedeDetalleEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeDetalleEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleSede manifiesto){

        ManifiestoSedeDetalleEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiesto());
        if(entity==null){
            entity = new ManifiestoSedeDetalleEntity();
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setCodigoMae(manifiesto.getCodigoMae());
            entity.setCodigo(manifiesto.getCodigo());
            entity.setNombreDesecho(manifiesto.getNombreDesecho());
            entity.setTotalBultos(manifiesto.getTotalBultos());

        }else if(entity!=null ){

            entity = new ManifiestoSedeDetalleEntity();entity.setCodigo(manifiesto.getCodigo());
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setCodigoMae(manifiesto.getCodigoMae());
            entity.setCodigo(manifiesto.getCodigo());
            entity.setNombreDesecho(manifiesto.getNombreDesecho());
            entity.setTotalBultos(manifiesto.getTotalBultos());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
