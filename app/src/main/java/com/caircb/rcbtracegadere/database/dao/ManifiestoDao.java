package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;

import java.util.List;

@Dao
public abstract class ManifiestoDao {


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos where estado=2 order by nombreCliente")
    public abstract List<RowItemHojaRuta> fetchHojaRutaPickIn();

    @Query("select * from tb_manifiestos where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select count(*) from tb_manifiestos where estado=1")
    public abstract int contarHojaRutaAsignadas();

    @Query("select count(*) from tb_manifiestos where estado=2")
    public abstract int contarHojaRutaProcesada();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos" +
            " where estado in(1,2) and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') order by nombreCliente")
    public abstract List<RowItemHojaRuta> fechManifiestoListaFiltrar(String search);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos where estado=1 order by nombreCliente")
    public abstract List<RowItemHojaRuta> fetchHojaRutaAsigando();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigando();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where estado=1 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManif(String search);

    @Query("update tb_manifiestos set numeroManifiesto=:numManifiesto, tecnicoFirmaImg=:img, tecnicoFirmaUrl=:url where idAppManifiesto=:idManifiesto")
    abstract void actualizarFirmaTecnicoGenerador(Integer idManifiesto, String numManifiesto, String img, String url);

    @Query("update tb_manifiestos set numeroManifiesto=:numManifiesto, transportistaFirmaImg=:img, transportistaFirmaUrl=:url where idAppManifiesto=:idManifiesto")
    abstract void actualizarFirmaTrasnportista(Integer idManifiesto, String numManifiesto, String img, String url);

    @Query("update tb_manifiestos set peso=:peso, nombreFirma=:nombreFirma, firmaImg=:firmaImg where idAppManifiesto =:idAppManifiesto")
    abstract void actualizarFirmaWithPesoTransportista(Integer idAppManifiesto, Double peso, String nombreFirma, String firmaImg);

    public void updateFirmaTecnicoGenerador(Integer idManifiesto, String numManifiesto, String img){
        actualizarFirmaTecnicoGenerador(idManifiesto,numManifiesto,img, AppDatabase.getFieldName(numManifiesto));
    }

    public void updateFirmaTransportsta(Integer idManifiesto, String numManifiesto, String img){
        actualizarFirmaTrasnportista(idManifiesto,numManifiesto,img, AppDatabase.getFieldName(numManifiesto));
    }

    public void updateFirmaWithPesoTransportista(Integer idManifiesto, Double peso, String nombreFimra, String firmaImg){
        actualizarFirmaWithPesoTransportista(idManifiesto, peso, nombreFimra, firmaImg);
    }

    @Query("update tb_manifiestos set idTecnicoGenerador=:idTecnicoGenerador where idAppManifiesto=:idManifiesto")
    public abstract void updateManifiestobyIdManifiesto(Integer idManifiesto, String idTecnicoGenerador);

    @Query("update tb_manifiestos set estado=:estado where idAppManifiesto=:id")
    public abstract void updateManifiestoEstadobyId(Integer id, Integer estado);

    @Query("delete from tb_manifiestos where idAppManifiesto=:idManifiesto")
    abstract void eliminarManifiestobyIdManifiesto(Integer idManifiesto);

    @Query("update tb_manifiestos set numManifiestoCliente=:idManifiestoCliente where idAppManifiesto=:id")
    public abstract void updateManifiestoCliente(Integer id, String idManifiestoCliente);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoEntity entity);

    public void saveOrUpdate(DtoManifiesto manifiesto){

        ManifiestoEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdAppManifiesto());
        if(entity==null && !manifiesto.getEliminado()){
            entity = new ManifiestoEntity();
            entity.setIdAppManifiesto(manifiesto.getIdAppManifiesto());
            entity.setNumManifiestoCliente(manifiesto.getNumManifiestoCliente());
            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setIdTecnicoGenerador(manifiesto.getIdTecnicoGenerador());
            entity.setTecnicoResponsable(manifiesto.getTecnicoResponsable());
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setTecnicoCelular(manifiesto.getTecnicoCelular());
            entity.setTecnicoCorreo(manifiesto.getTecnicoCorreo());
            // resolutivoInstalacion
            entity.setNumeroGeneradorDesecho(manifiesto.getNumeroGeneradorDesecho());
            entity.setLicenciaAmbiental(manifiesto.getNumLicenciaAmbiental());
            // idCliente
            entity.setIdentificacionCliente(manifiesto.getIdentificacionCliente());
            entity.setNombreCliente(manifiesto.getRazonSocial());
            entity.setDireccionCliente(manifiesto.getDireccion());
            entity.setProvincia(manifiesto.getProvincia());
            entity.setCanton(manifiesto.getCanton());
            entity.setParroquia(manifiesto.getParroquia());
            //telefono
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setIdLugar(manifiesto.getIdLugar());
            // nombreLugar
            // idChoferRecolector
            // idAuxiliarRecolector;
            entity.setConductorIdentificacion(manifiesto.getIdentificacionChoferRecolector());
            entity.setConductorNombre(manifiesto.getNombreChoferRecolector());
            entity.setAuxiliarIdentificacion(manifiesto.getIdentificacionAuxiliarRecolector());
            entity.setAuxiliarNombre(manifiesto.getNombreAuxiliarRecolector());
            // idTransporte
            //identificacionTransportista
            //razonSocialTransportista
            //numeroLicenciaAmbientalTransportista
            //idTransporteVehiculo
            //numeroPlacaVehiculo
            //modeloVehiculo
            //idDestinatario
            //identificacionDestinatario
            //razonSocialDestinatario
            //numeroLicenciaAmbientalDestinatario
            entity.setEstado(manifiesto.getEstadoApp());
            entity.setFechaManifiesto(manifiesto.getFechaTemp());
            entity.setTipoPaquete(manifiesto.getTipoPaquete());

            entity.setPeso(manifiesto.getPeso());
            entity.setNombreFirma(manifiesto.getNombreFirma());
            entity.setFirmaImg(manifiesto.getFirmaImg());

        }else if(entity!=null && !manifiesto.getEliminado() ){

            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNumManifiestoCliente(manifiesto.getNumManifiestoCliente());
            entity.setIdTecnicoGenerador(manifiesto.getIdTecnicoGenerador());
            entity.setTecnicoResponsable(manifiesto.getTecnicoResponsable());
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setTecnicoCelular(manifiesto.getTecnicoCelular());
            entity.setTecnicoCorreo(manifiesto.getTecnicoCorreo());
            // resolutivoInstalacion
            entity.setNumeroGeneradorDesecho(manifiesto.getNumeroGeneradorDesecho());
            entity.setLicenciaAmbiental(manifiesto.getNumLicenciaAmbiental());
            // idCliente
            entity.setIdentificacionCliente(manifiesto.getIdentificacionCliente());
            entity.setNombreCliente(manifiesto.getRazonSocial());
            entity.setDireccionCliente(manifiesto.getDireccion());
            entity.setProvincia(manifiesto.getProvincia());
            entity.setCanton(manifiesto.getCanton());
            entity.setParroquia(manifiesto.getParroquia());
            //telefono
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setIdLugar(manifiesto.getIdLugar());
            // nombreLugar
            // idChoferRecolector
            // idAuxiliarRecolector;
            entity.setConductorIdentificacion(manifiesto.getIdentificacionChoferRecolector());
            entity.setConductorNombre(manifiesto.getNombreChoferRecolector());
            entity.setAuxiliarIdentificacion(manifiesto.getIdentificacionAuxiliarRecolector());
            entity.setAuxiliarNombre(manifiesto.getNombreAuxiliarRecolector());
            // idTransporte
            //identificacionTransportista
            //razonSocialTransportista
            //numeroLicenciaAmbientalTransportista
            //idTransporteVehiculo
            //numeroPlacaVehiculo
            //modeloVehiculo
            //idDestinatario
            //identificacionDestinatario
            //razonSocialDestinatario
            //numeroLicenciaAmbientalDestinatario
            entity.setEstado(manifiesto.getEstadoApp());
            entity.setFechaManifiesto(manifiesto.getFechaTemp());
            entity.setTipoPaquete(manifiesto.getTipoPaquete());

            entity.setPeso(manifiesto.getPeso());
            entity.setNombreFirma(manifiesto.getNombreFirma());
            entity.setFirmaImg(manifiesto.getFirmaImg());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
