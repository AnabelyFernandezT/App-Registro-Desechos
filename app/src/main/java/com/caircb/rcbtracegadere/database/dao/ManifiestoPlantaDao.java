package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;

import java.util.List;

@Dao
public abstract class ManifiestoPlantaDao {

    @Query("select * from tb_manifiestos_planta where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoPlantaEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC " )
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif();

    @Query("select MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_Planta_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos " +
            "            from tb_manifiestos_planta MC  " +
            "where (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif(String search);


    @Query("delete from tb_manifiestos_planta where idAppManifiesto=:idManifiesto")
    abstract void eliminarManifiestobyIdManifiesto(Integer idManifiesto);

    @Query("delete from tb_manifiestos_planta ")
    public abstract void eliminarManifiestos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoPlantaEntity entity);

    public void saveOrUpdate(DtoManifiestoSede manifiesto){

        ManifiestoPlantaEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdAppManifiesto());
        if(entity==null ){
            entity = new ManifiestoPlantaEntity();
            entity.setIdAppManifiesto(manifiesto.getIdAppManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
        }
        else if(entity!=null  ){
            entity = new ManifiestoPlantaEntity();
            entity.setIdAppManifiesto(manifiesto.getIdAppManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
        }


        if (entity!=null) createManifiesto(entity);
    }



}
