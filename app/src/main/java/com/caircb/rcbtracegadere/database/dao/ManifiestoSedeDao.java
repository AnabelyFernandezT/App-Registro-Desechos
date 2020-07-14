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
import com.caircb.rcbtracegadere.models.ItemManifiestoPlantaCodigoQR;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDao {

    @Query("select * from tb_manifiestos_sede where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoSedeEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_sede MC ")
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif();

    @Query("select MC.estado,MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente," +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 and M.idTransporteVehiculo=:vehiculo) as bultosSelecionado, " +
            "            (SELECT COUNT(idManifiestoDetalleValor) " +
            "            FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                                       INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "            WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos " +
            "            from tb_manifiestos_sede MC  " +
            "where (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiestoSede> fetchManifiestosAsigByClienteOrNumManif(String search, Integer vehiculo);

    @Query("select count(*) from tb_manifiestos_sede")
    public abstract int contarHojaRutaAsignadas();

    @Query("select MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,DTVC.peso, DTC.nombreDesecho, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_sede_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto and DTV.estado = 1 ) as bultosSelecionado, " +
            "(SELECT COUNT(idManifiestoDetalleValor) " +
            "FROM tb_manifiestos_sede M INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DT ON M.idAppManifiesto=DT.idAppManifiesto " +
            "                           INNER JOIN  tb_manifiestos_sede_det_valor DTV ON DT.idManifiestoDetalle = DTV.idManifiestoDetalle " +
            "WHERE MC.idAppManifiesto = M.idAppManifiesto) as totalBultos "+
            "from tb_manifiestos_sede MC INNER JOIN TB_MANIFIESTOS_SEDE_DETALLE DTC ON MC.idAppManifiesto=DTC.idAppManifiesto " +
            "                              INNER JOIN  tb_manifiestos_sede_det_valor DTVC ON DTC.idManifiestoDetalle = DTVC.idManifiestoDetalle " +
            "WHERE DTVC.codigoQR=:codigoQR " +
            "Group by MC.idAppManifiesto,MC.numeroManifiesto ,MC.nombreCliente,DTVC.peso, DTC.nombreDesecho")
    @Transaction
    public abstract ItemManifiestoPlantaCodigoQR fetchManifiestosBultos(String codigoQR);


    @Query("Select estado from tb_manifiestos_sede where idAppManifiesto=:idManifiesto")
    @Transaction
    public abstract int estadoManifiestoSede(Integer idManifiesto);

    @Query("update tb_manifiestos_sede set estado=3 where idAppManifiesto=:idManifiesto")
    @Transaction
    public abstract int updateEstadoManifiesto(Integer idManifiesto);



    @Query("delete from tb_manifiestos_sede where idAppManifiesto=:idManifiesto")
    abstract void eliminarManifiestobyIdManifiesto(Integer idManifiesto);

    @Query("delete from tb_manifiestos_sede ")
    public abstract void eliminarManifiestos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeEntity entity);

    public void saveOrUpdate(DtoManifiestoSede manifiesto){

        ManifiestoSedeEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdAppManifiesto());
        if(entity==null ){
            entity = new ManifiestoSedeEntity();
            entity.setIdAppManifiesto(manifiesto.getIdAppManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
            entity.setEstado(manifiesto.getEstado());
            entity.setBultosRegistrados(manifiesto.getBultosRegistrados());
            entity.setBultosTotal(manifiesto.getBultosTotal());
        }
        else if(entity!=null  ){
            entity.setIdAppManifiesto(manifiesto.getIdAppManifiesto());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNombreCliente(manifiesto.getNombreCliente());
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
            entity.setEstado(manifiesto.getEstado());
            entity.setBultosRegistrados(manifiesto.getBultosRegistrados());
            entity.setBultosTotal(manifiesto.getBultosTotal());
        }


        if (entity!=null) createManifiesto(entity);
    }



}
