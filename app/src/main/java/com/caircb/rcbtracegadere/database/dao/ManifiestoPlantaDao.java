package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoPlantaCodigoQR;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;

import java.util.List;

@Dao
public abstract class ManifiestoPlantaDao {

    @Query("select count(*) from tb_manifiestos_planta where idTransporteVehiculo=:idVehiculo")
    public abstract int contarHojaRutaProcesada(Integer idVehiculo);

    @Query("select * from tb_manifiestos_planta where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoPlantaEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    /******************/
    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_Planta_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos " +
            "            from tb_manifiestos_planta MC  " +
            "where MC.estado=4 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosPendientesXpesarSearch(String search);


    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC  WHERE MC.estado = 4 " )
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosPendientesXpesar();

    /*******************/
    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC  where idTransporteVehiculo =:idtransporte " +
            "Order by estado asc" )
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif(Integer idtransporte);

    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC  " )
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManifCodigoQR();

    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC where idAppManifiesto =:idManifiesto " )
    @Transaction
    public abstract ItemManifiestoSede fetchManifiestosAsigByTotal( Integer idManifiesto);

    @Query("select MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,DTVC.peso, DTC.nombreDesecho, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_planta MC INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DTC ON MC.idAppManifiesto=DTC.idAppManifiesto " +
            "                              INNER JOIN  tb_manifiestos_planta_det_valor DTVC ON DTC.idManifiestoDetalle = DTVC.idManifiestoDetalle " +
            "            WHERE DTVC.codigoQR=:codigoQR "+
            "GROUP BY MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,DTVC.peso, DTC.nombreDesecho")
    @Transaction
    public abstract ItemManifiestoPlantaCodigoQR fetchManifiestosBultos(String codigoQR);


    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
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

    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,idTransporteVehiculo, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_Planta_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_planta M INNER JOIN TB_MANIFIESTOS_PLANTA_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_planta_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos " +
            "            from tb_manifiestos_planta MC  " +
            "where idTransporteVehiculo=:idVehiculo and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  " +
            "Order by estado asc")
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManifPlanta(String search, Integer idVehiculo);


    @Query("update tb_manifiestos_planta set estado=3  where idAppManifiesto=:idManifiesto")
    public abstract void updateEstadoManifiesto(Integer idManifiesto);

    @Query("select estado from tb_manifiestos_planta where idAppManifiesto = :idManifiesto ")
    public abstract Integer obtenerEstadoManifiesto(Integer idManifiesto);

    @Query("delete from tb_manifiestos_planta where idAppManifiesto=:idManifiesto")
    abstract void eliminarManifiestobyIdManifiesto(Integer idManifiesto);

    @Query("delete from tb_manifiestos_planta ")
    public abstract void eliminarManifiestos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoPlantaEntity entity);

    public void saveOrUpdate(DtoManifiestoPlanta manifiesto){

        ManifiestoPlantaEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiesto());
        if(entity==null ){
            entity = new ManifiestoPlantaEntity();
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
            entity.setEstado(manifiesto.getEstado());
            entity.setBultosRegistrados(manifiesto.getBultosRegistrados());
            entity.setBultosTotal(manifiesto.getBultosTotal());
            entity.setIdLoteContenedor(manifiesto.getIdLoteContenedor());
        }
        else if(entity!=null  ){
            entity.setIdAppManifiesto(manifiesto.getIdManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
            entity.setEstado(manifiesto.getEstado());
            entity.setBultosRegistrados(manifiesto.getBultosRegistrados());
            entity.setBultosTotal(manifiesto.getBultosTotal());
            entity.setIdLoteContenedor(manifiesto.getIdLoteContenedor());
        }


        if (entity!=null) createManifiesto(entity);
    }



}
