package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiestoPrint;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;

import java.util.List;

@Dao
public abstract class ManifiestoDetalleDao {

    @Query("select _id as id,'Unidad' as unidad, 1.7 as peso, 'Descriocion' as descripcion,'Tratamiento'" +
            " as tratamiento,1.8 as cantidadBulto,tipoItem,tipoPaquete,estadoChek as estado , 'SI' as devolucionRecp  " +
            "from  tb_manifiestos_detalle where _id in(1)")
    public abstract List<RowItemManifiestoPrint> searhItemPrint();

    @Query("update tb_manifiestos_detalle set estadoChek=:check, codeQr=:codigo where idAppManifiestoDetalle=:idManifiestoDetalle ")
    public abstract void updateManifiestoDetallebyId(Integer idManifiestoDetalle, boolean check, String codigo);

    @Query("update tb_manifiestos_detalle set cantidadBulto=:cantidadBulto, pesoUnidad=:peso, estadoChek=:estadoChek,cantidadTotalEtiqueta=:cantidad where idAppManifiestoDetalle=:idManifiestoDetalle")
    public abstract void updateCantidadBultoManifiestoDetalle(Integer idManifiestoDetalle, double cantidadBulto, double peso,Integer cantidad,boolean estadoChek);

    @Query("select d.idAppManifiestoDetalle as id,cd.nombre as descripcion,'' as unidad,cd.codigo, d.pesoUnidad as peso, d.cantidadBulto,d.estadoChek as estado, tratamiento, tipoItem,tipoPaquete" +
            " from tb_manifiestos_detalle d" +
            " inner join tb_catalogos cd on d.idTipoDesecho=cd.idSistema and cd.tipo=2" +
            " where idAppManifiesto=:idManifiesto")
    public abstract List<RowItemManifiesto> fetchHojaRutaDetallebyIdManifiesto(Integer idManifiesto);

    @Query("update tb_manifiestos_detalle set pesoUnidad=:pesoUnidad, estadoChek=:check where idAppManifiestoDetalle=:idManifiestoDetalle")
    abstract void actualizarPesoManifiestoDetalle(Integer idManifiestoDetalle, double pesoUnidad, boolean check);

    public void updatePesoManifiestoDetalle(Integer idManifiestoDetalle, double pesoUnidad){
        actualizarPesoManifiestoDetalle(idManifiestoDetalle,pesoUnidad,pesoUnidad>0);
    }

    @Query("select * from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto")
    public abstract List<ManifiestoDetalleEntity> fecthConsultarManifiestoDetallebyID(Integer idManifiesto);

    @Query("select * from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto and estadoChek=1")
    public abstract List<ManifiestoDetalleEntity> fecthConsultarManifiestoDetalleSeleccionados(Integer idManifiesto);


    @Query("select * from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDt")
    public abstract ManifiestoDetalleEntity fetchHojaRutaDetbyIdManifiestoDet(Integer idManifiesto, Integer idManifiestoDt);

    @Query("select * from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto and estadoChek = 1")
    public abstract ManifiestoDetalleEntity fetchPesoManifiesto(Integer idManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoDetalle(ManifiestoDetalleEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalle dt){
        ManifiestoDetalleEntity entity;

        entity = fetchHojaRutaDetbyIdManifiestoDet(dt.getIdAppManifiesto(),dt.getIdAppManifiestoDetalle());
        if(entity==null){

            entity = new ManifiestoDetalleEntity();
            entity.setIdAppManifiesto(dt.getIdAppManifiesto());
            entity.setIdAppManifiestoDetalle(dt.getIdAppManifiestoDetalle());
            entity.setIdTipoDesecho(dt.getIdTipoDesecho());
            entity.setIdTipoUnidad(dt.getIdTipoUnidad());
            entity.setPesoUnidad(dt.getPesoUnidad());
            entity.setCantidadDesecho(dt.getCantidadDesecho());
            entity.setEstadoChek(false);
            entity.setCantidadBulto(0);
            entity.setTipoItem(dt.getPesajeBultoFlag());
            entity.setTipoPaquete(dt.getTipoPaquete());
            entity.setCantidadTotalEtiqueta(0);
            entity.setIdDestinatario(dt.getIdDestinatario());

        }else{
            entity.setIdTipoDesecho(dt.getIdTipoDesecho());
            entity.setIdTipoUnidad(dt.getIdTipoUnidad());
            entity.setPesoUnidad(dt.getPesoUnidad());
            entity.setCantidadDesecho(dt.getCantidadDesecho());
            entity.setTipoItem(dt.getPesajeBultoFlag());
            entity.setTipoPaquete(dt.getTipoPaquete());
            entity.setIdDestinatario(dt.getIdDestinatario());
            //entity.setEstadoChek(false);
            //entity.setCantidadBulto(0);
        }

        createManifiestoDetalle(entity);

    }

}
