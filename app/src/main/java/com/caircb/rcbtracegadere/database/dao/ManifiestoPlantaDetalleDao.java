package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaDetalleEntity;
import com.caircb.rcbtracegadere.models.DtoDetallesBultoPlanta;
import com.caircb.rcbtracegadere.models.DtoDetallesPlanta;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;

import java.util.List;

@Dao
public abstract class ManifiestoPlantaDetalleDao {
    @Query("delete from tb_manifiestos_planta_detalle")
    @Transaction
    public abstract void eliminarDetalle();

    @Query("select idManifiestoDetalle,codigoMae,codigo,nombreDesecho,totalBultos, " +
            "(SELECT COUNT(idManifiestoDetalleValor) FROM tb_manifiestos_planta_det_valor DTV WHERE DT.idManifiestoDetalle = DTV.idManifiestoDetalle and DTV.estado = 1 )as bultosSelecionado " +
            "from tb_manifiestos_planta_detalle DT "+
            "where idAppManifiesto=:idManifiesto" )
    @Transaction
    public abstract List<ItemManifiestoDetalleSede> fetchManifiestosAsigByClienteOrNumManif(Integer idManifiesto);

    @Query("select idManifiestoDetalle as idDetalle from tb_manifiestos_planta_detalle where idAppManifiesto=:idManifiesto" )
    @Transaction
    public abstract List<Integer> fetchManifiestosAsigDetalle(Integer idManifiesto);

    @Query("select idManifiestoDetalleValor as idDetalleValor, nuevoPeso as peso from tb_manifiestos_planta_det_valor where idManifiestoDetalle=:idManifiesto" )
    @Transaction
    public abstract List<DtoDetallesBultoPlanta> fetchManifiestosAsigDetalleBultos(Integer idManifiesto);

    @Query("select * from tb_manifiestos_planta_detalle where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoPlantaDetalleEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoPlantaDetalleEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleSede manifiesto){

        ManifiestoPlantaDetalleEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiesto());
        if(entity==null){
            entity = new ManifiestoPlantaDetalleEntity();
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setCodigoMae(manifiesto.getCodigoMae());
            entity.setCodigo(manifiesto.getCodigo());
            entity.setNombreDesecho(manifiesto.getNombreDesecho());
            entity.setTotalBultos(manifiesto.getTotalBultos());

        }else if(entity!=null ){

            entity = new ManifiestoPlantaDetalleEntity();entity.setCodigo(manifiesto.getCodigo());
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
