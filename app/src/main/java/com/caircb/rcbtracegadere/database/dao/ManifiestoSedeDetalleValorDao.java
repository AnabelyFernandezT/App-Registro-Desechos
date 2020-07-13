package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDetalleValorDao {

    @Query("delete from tb_manifiestos_sede_det_valor")
    @Transaction
    public abstract void eliminarDetalle();

    @Query("select count(idManifiestoDetalleValor) from tb_manifiestos_sede_det_valor where idManifiestoDetalle=:idManifiesto and estado = 1 " )
    @Transaction
    public abstract Integer fetchNumeroTotalAsigByManifiesto(Integer idManifiesto);

    @Query("Select estado from tb_manifiestos_sede_det_valor where codigoQR=:codigoQR ")
    @Transaction
    public abstract Boolean verificarBultoEstado(String codigoQR);

    @Query("UPDATE tb_manifiestos_sede_det_valor SET estado = 1 and estadoEnvio = 1 where codigoQR=:codigoQR ")
    @Transaction
    public abstract void actualizarBultoEstado(String codigoQR);

    @Query("select idManifiestoDetalle,idManifiestoDetalleValor as idManifiestoDetalleValores,peso,codigoQR,nombreBulto,estado from tb_manifiestos_sede_det_valor where idManifiestoDetalle=:idManifiesto" )
    @Transaction
    public abstract List<ItemManifiestoDetalleValorSede> fetchManifiestosAsigByClienteOrNumManif(Integer idManifiesto);

    @Query("update tb_manifiestos_sede_det_valor set estado=:check , estadoEnvio = 1 where idManifiestoDetalle=:idManifiestoDetalle and idManifiestoDetalleValor=:idManifiestoDetalleValores  ")
    public abstract void updateManifiestoDetalleValorSedebyId(Integer idManifiestoDetalle, boolean check, Integer idManifiestoDetalleValores);

    @Query("select * from tb_manifiestos_sede_det_valor where idManifiestoDetalle=:idManifiesto limit 1")
    public abstract ManifiestoSedeDetalleValorEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select idManifiestoDetalleValor from tb_manifiestos_sede_det_valor where estado = 1 and estadoEnvio = 1")
    public abstract List<Integer> fetchDetallesRecolectados();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeDetalleValorEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleValorSede manifiesto){

        ManifiestoSedeDetalleValorEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiestoDetalle());
        if(entity==null){
            Boolean estado = manifiesto.getEstado() ==1 ? true:false;
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(estado);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValores());
            entity.setNombreBulto(manifiesto.getNombreBulto());
            entity.setEstadoEnvio(0);
        }else if(entity!=null  ){
            Boolean estado = manifiesto.getEstado() ==1 ? true:false;
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(estado);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValores());
            entity.setNombreBulto(manifiesto.getNombreBulto());
            entity.setEstadoEnvio(0);
        }

        if (entity!=null) createManifiesto(entity);
    }



}
