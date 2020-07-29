package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiestoDetalle;
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

    @Query("update tb_manifiestos_detalle set tipoBalanza =:idTipoBalanza where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idDetalleManifiesto")
    public abstract  void updateTipoBalanzaByDetalleId(Integer idManifiesto, Integer idDetalleManifiesto, Integer idTipoBalanza);

    @Query("select d.idAppManifiestoDetalle as id,cd.nombre as descripcion,'' as unidad,cd.codigo, d.pesoUnidad as peso, d.cantidadBulto,d.estadoChek as estado, tratamiento, tipoItem,tipoPaquete , tipoBalanza, pesoReferencial, faltaImpresiones" +
            " from tb_manifiestos_detalle d" +
            " inner join tb_catalogos cd on d.idTipoDesecho=cd.idSistema and cd.tipo=2" +
            " where idAppManifiesto=:idManifiesto")
    public abstract List<RowItemManifiesto> fetchHojaRutaDetallebyIdManifiesto(Integer idManifiesto);

    @Query("update tb_manifiestos_detalle set faltaImpresiones=:bandera where idAppManifiesto =:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle")
    public abstract void updateFlagFaltaImpresiones(Integer idManifiesto, Integer idManifiestoDetalle, boolean bandera);

    @Query("update tb_manifiestos_detalle set faltaImpresiones=:bandera where idAppManifiesto =:idManifiesto")
    public abstract void updateFlagFaltaImpresionesByIdManifiesto(Integer idManifiesto, boolean bandera);

    @Query("select count(*) from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto and faltaImpresiones=1")
    public abstract Integer countDetallesSinImprimirByIdManifiesto(Integer idManifiesto);

    @Query("select d.idAppManifiestoDetalle as id,cd.nombre as descripcion,'' as unidad,cd.codigo, d.pesoUnidad as peso, d.cantidadBulto,d.estadoChek as estado, tratamiento, tipoItem,tipoPaquete , tipoBalanza, pesoReferencial,codigoMAE as codigoMae, " +
            "estadoFisico as estadoFisico, tipoContenedor as tipoContenedor," +
            "residuoSujetoFiscalizacion,requiereDevolucionRecipientes,tieneDisponibilidadMontacarga,tieneDisponibilidadBalanza,requiereIncineracionPresenciada,observacionResiduos" +
            " from tb_manifiestos_detalle d" +
            " inner join tb_catalogos cd on d.idTipoDesecho=cd.idSistema and cd.tipo=2" +
            " where idAppManifiesto=:idManifiesto")
    public abstract List<RowItemManifiestoDetalle> fetchHojaRutaDetallebyIdManifiesto2(Integer idManifiesto);

    @Query("update tb_manifiestos_detalle set pesoUnidad=:pesoUnidad, estadoChek=:check where idAppManifiestoDetalle=:idManifiestoDetalle")
    abstract void actualizarPesoManifiestoDetalle(Integer idManifiestoDetalle, double pesoUnidad, boolean check);

    public void updatePesoManifiestoDetalle(Integer idManifiestoDetalle, double pesoUnidad){
        actualizarPesoManifiestoDetalle(idManifiestoDetalle,pesoUnidad,pesoUnidad>0);
    }

    @Query("select * from tb_manifiestos_detalle where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle LIMIT 1")
    public abstract ManifiestoDetalleEntity fecthConsultarManifiestoDetallebyID(Integer idManifiesto, Integer idManifiestoDetalle);

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
            entity.setCodigoMAE(dt.getCodigoMAE());
            entity.setNombreDesecho(dt.getNombreDesecho());
            entity.setNombreDestinatario(dt.getNombreDestinatario());
            entity.setPesoReferencial(dt.getPesoReferencial());
            entity.setTipoBalanza(0);
            entity.setTratamiento(dt.getTratamiento());
            entity.setValidadorReferencial(dt.getValidadorReferencial());
            entity.setTipoContenedor(dt.getTipoContenedor());
            entity.setEstadoFisico(dt.getEstadoFisico());
            entity.setFaltaImpresiones(false);

            entity.setEstadoPaquete(dt.getEstado());
            entity.setResiduoSujetoFiscalizacion(dt.getResiduoSujetoFiscalizacion());
            entity.setRequiereDevolucionRecipientes(dt.getRequiereDevolucionRecipientes());
            entity.setTieneDisponibilidadMontacarga(dt.getTieneDisponibilidadMontacarga());
            entity.setTieneDisponibilidadBalanza(dt.getTieneDisponibilidadBalanza());
            entity.setRequiereIncineracionPresenciada(dt.getRequiereIncineracionPresenciada());
            entity.setObservacionResiduos(dt.getObservacionResiduos());

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
            entity.setCodigoMAE(dt.getCodigoMAE());
            entity.setNombreDesecho(dt.getNombreDesecho());
            entity.setNombreDestinatario(dt.getNombreDestinatario());
            entity.setValidadorReferencial(dt.getValidadorReferencial());
            entity.setTipoContenedor(dt.getTipoContenedor());
            entity.setEstadoFisico(dt.getEstadoFisico());

            entity.setEstadoPaquete(dt.getEstado());
            entity.setResiduoSujetoFiscalizacion(dt.getResiduoSujetoFiscalizacion());
            entity.setRequiereDevolucionRecipientes(dt.getRequiereDevolucionRecipientes());
            entity.setTieneDisponibilidadMontacarga(dt.getTieneDisponibilidadMontacarga());
            entity.setTieneDisponibilidadBalanza(dt.getTieneDisponibilidadBalanza());
            entity.setRequiereIncineracionPresenciada(dt.getRequiereIncineracionPresenciada());
            entity.setObservacionResiduos(dt.getObservacionResiduos());
        }

        createManifiestoDetalle(entity);

    }

}
