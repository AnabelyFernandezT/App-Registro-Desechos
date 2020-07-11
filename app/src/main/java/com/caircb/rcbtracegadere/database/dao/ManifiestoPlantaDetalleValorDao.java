package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaDetalleValorEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;

import java.util.List;

@Dao
public abstract class ManifiestoPlantaDetalleValorDao {

    @Query("delete from tb_manifiestos_planta_det_valor")
    @Transaction
    public abstract void eliminarDetalle();

    @Query("select count(idManifiestoDetalleValor) from tb_manifiestos_planta_det_valor where idManifiestoDetalle=:idManifiesto and estado = 1 " )
    @Transaction
    public abstract Integer fetchNumeroTotalAsigByManifiesto(Integer idManifiesto);

    @Query("Select estado from tb_manifiestos_planta_det_valor where codigoQR=:codigoQR ")
    @Transaction
    public abstract Boolean verificarBultoEstado(String codigoQR);

    @Query("UPDATE tb_manifiestos_planta_det_valor SET estado = 1 where codigoQR=:codigoQR ")
    @Transaction
    public abstract void actualizarBultoEstado(String codigoQR);

    @Query("select idManifiestoDetalle,idManifiestoDetalleValor as idManifiestoDetalleValores,peso,codigoQR,nombreBulto,estado,nuevoPeso from tb_manifiestos_planta_det_valor where idManifiestoDetalle=:idManifiesto" )
    @Transaction
    public abstract List<ItemManifiestoDetalleValorSede> fetchManifiestosAsigByClienteOrNumManif(Integer idManifiesto);

    @Query("update tb_manifiestos_planta_det_valor set estado=:check where idManifiestoDetalle=:idManifiestoDetalle and idManifiestoDetalleValor=:idManifiestoDetalleValores  ")
    public abstract void updateManifiestoDetalleValorSedebyId(Integer idManifiestoDetalle, boolean check, Integer idManifiestoDetalleValores);

    @Query("update tb_manifiestos_planta_det_valor set nuevoPeso=:pesoNuevo where idManifiestoDetalle=:idManifiestoDetalle and idManifiestoDetalleValor=:idManifiestoDetalleValores  ")
    public abstract void updateManifiestoDetalleValorPlantaPesoNuevo(Integer idManifiestoDetalle, String pesoNuevo, Integer idManifiestoDetalleValores);

    @Query("select * from tb_manifiestos_planta_det_valor where idManifiestoDetalle=:idManifiesto limit 1")
    public abstract ManifiestoPlantaDetalleValorEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select idManifiestoDetalleValor from tb_manifiestos_planta_det_valor where estado = 1")
    public abstract List<Integer> fetchDetallesRecolectados();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoPlantaDetalleValorEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleValorSede manifiesto){

        ManifiestoPlantaDetalleValorEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiestoDetalle());
        if(entity==null){
            entity = new ManifiestoPlantaDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(false);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValores());
            entity.setNombreBulto(manifiesto.getNombreBulto());

        }else if(entity!=null  ){
            entity = new ManifiestoPlantaDetalleValorEntity();
            entity.setIdManifiestoDetalle(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setEstado(false);
            entity.setIdManifiestoDetalleValor(manifiesto.getIdManifiestoDetalleValores());
            entity.setNombreBulto(manifiesto.getNombreBulto());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
