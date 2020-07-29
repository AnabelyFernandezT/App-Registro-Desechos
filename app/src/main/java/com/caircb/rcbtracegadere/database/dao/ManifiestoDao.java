package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.models.ItemFirmasManifiesto;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoDao {


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos where estado=2 order by nombreCliente")
    public abstract List<RowItemHojaRuta> fetchHojaRutaPickIn();

    @Query("select * from tb_manifiestos where idAppManifiesto=:idManifiesto limit 1")
    public abstract ManifiestoEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

    @Query("select * from tb_manifiestos where idTransporteVehiculo=:idVehiculo limit 1")
    public abstract ManifiestoEntity fetchHojaRutabyIdTransporte(Integer idVehiculo);

    @Query("select * from tb_manifiestos limit 1" )
    public abstract ManifiestoEntity fetchHojaRuta();

    @Query("select * from tb_manifiestos where idChoferRecolector=:idTransportista limit 1" )
    public abstract ManifiestoEntity fetchHojaRutaEn(Integer idTransportista);

    @Query("select count(*) from tb_manifiestos where estado=1 ")
    public abstract int contarHojaRutaAsignadas();

    @Query("select count(*) from tb_manifiestos where estado=2 and idChoferRecolector=:idConductor ")
    public abstract int contarHojaRutaAsignadasP(Integer idConductor);

    @Query("select count(*) from tb_manifiestos where estado=2 and idChoferRecolector=:idConductor  and idSubRuta=:idSubRuta ")
    public abstract int contarHojaRutaAsignadasByIdConductorAndRuta(Integer idConductor, Integer idSubRuta);

    @Query("select count(*) from tb_manifiestos where estado=1 and idChoferRecolector=:idChoferRecolector ")
    public abstract int contarHojaRutaAsignadasPara(Integer idChoferRecolector);

    @Query("select count(*) from tb_manifiestos where estado<>1 ")
    public abstract int contarHojaRutaProcesada();

    @Query("select count(*) from tb_manifiestos where estado=2 and idTransporteVehiculo =:idVehiculo") /*** se quito and estadoFinRuta=0  ****/
    public abstract int contarHojaRutaProcesadaPlanta(Integer idVehiculo);

    @Query("select count(*) from tb_manifiestos where estado<>1 and idSubRuta=:idSubruta and idChoferRecolector=:idChoferRecolector ")
    public abstract int contarHojaRutaProcesadaPara(Integer idSubruta, Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos" +
            " where estado in(1,2) and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') order by nombreCliente")
    public abstract List<RowItemHojaRuta> fechManifiestoListaFiltrar(String search);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos where estado=1 order by nombreCliente")
    public abstract List<RowItemHojaRuta> fetchHojaRutaAsigando();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigando();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado, apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal, tecnicoTelefono as telefono, frecuencia as frecuencia, tipoPaquete as tipoPaquete from tb_manifiestos where estado=1 and idSubRuta=:idSubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandobySubRuta(Integer idSubRuta, Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado, apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal, tecnicoTelefono as telefono, frecuencia as frecuencia, tipoPaquete as tipoPaquete from tb_manifiestos where estado=1 and idTransporteVehiculo=:idSubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandobySubRutaPlanta(Integer idSubRuta, Integer idChoferRecolector);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1000 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosBuscarData();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1000 and idSubRuta=:SubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosBuscarDataByRuta(Integer SubRuta,Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado,apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal, tecnicoTelefono as telefono, frecuencia as frecuencia, tipoPaquete as tipoPaquete from tb_manifiestos where estado in(2) and idSubRuta=:Subruta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosProcesados(Integer Subruta, Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1 and idTransporteVehiculo =:idPlaca order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandoByPlaca(Integer idPlaca);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion," +
            " provincia as provincia, canton as canton, estado," +
            " apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal " +
            "from tb_manifiestos where estado=2 and idTransporteVehiculo=:idPlaca  order by nombreCliente") /***and estadoFinRuta=0***/
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandoPlanta(Integer idPlaca);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion," +
            "provincia as provincia, canton as canton, estado," +
            " apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal " +
            "from tb_manifiestos " +
            "where estado=1 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') and idSubRuta=:SubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManif(String search,Integer SubRuta,Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado,+" +
            " apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal from tb_manifiestos " +
            "where estado=1 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') and idTransporteVehiculo=:SubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManifPlantaXNO(String search,Integer SubRuta,Integer idChoferRecolector);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado,+" +
            " apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal from tb_manifiestos " +
            "where estado=2 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') and idTransporteVehiculo=:idVehiculo order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManifPlanta(String search,Integer idVehiculo);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where estado=2 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') and idSubRuta=:SubRuta and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumProcesado(String search,Integer SubRuta,Integer idChoferRecolector);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, sucursal as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado, apertura1 as Apertura1,apertura2 as Apertura2,cierre1 as Cierre1,cierre2 as Cierre2, sucursal as sucursal, tecnicoTelefono as telefono, frecuencia as frecuencia, tipoPaquete as tipoPaquete from tb_manifiestos " +
            "where (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  and idSubRuta=:SubRuta  and idChoferRecolector=:idChoferRecolector order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManifSearch(String search, Integer SubRuta,Integer idChoferRecolector);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where estado=2 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%') and estadoFinRuta = 0 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigPlanta(String search);

    /*
    @Query("update tb_manifiestos set numeroManifiesto=:numManifiesto, tecnicoFirmaImg=:img, tecnicoFirmaUrl=:url where idAppManifiesto=:idManifiesto")
    abstract void actualizarFirmaTecnicoGenerador(Integer idManifiesto, String numManifiesto, String img, String url);
    */
    /*
    @Query("update tb_manifiestos set numeroManifiesto=:numManifiesto, transportistaFirmaImg=:img, transportistaFirmaUrl=:url where idAppManifiesto=:idManifiesto")
    abstract void actualizarFirmaTrasnportista(Integer idManifiesto, String numManifiesto, String img, String url);
    */
    /*
    @Query("update tb_manifiestos set peso=:peso, nombreFirma=:nombreFirma, firmaImg=:firmaImg where idAppManifiesto =:idAppManifiesto")
    abstract void actualizarFirmaWithPesoTransportista(Integer idAppManifiesto, Double peso, String nombreFirma, String firmaImg);
    */
    @Query("update tb_manifiestos set IdTecnicoGenerador=:IdTecnicoGenerador where idAppManifiesto =:idAppManifiesto")
    abstract void actualizarTecnicoGenerador(Integer idAppManifiesto, Integer IdTecnicoGenerador);


    public void updateGenerador(Integer idManifiesto, Integer IdTecnicoGenerador){
        actualizarTecnicoGenerador(idManifiesto, IdTecnicoGenerador);
    }
    @Query("update tb_manifiestos set novedadEncontrada=:novedadEncontrada where idAppManifiesto=:idAppManifiesto")
    abstract void actualizarNovedadEncontrada (Integer idAppManifiesto , String novedadEncontrada);

    public void updateNovedadEncontrada(Integer idManifiesto, String novedadEncontrada){
        actualizarNovedadEncontrada(idManifiesto, novedadEncontrada);
    }


    /*
    public void updateFirmaTecnicoGenerador(Integer idManifiesto, String numManifiesto, String img){

        actualizarFirmaTecnicoGenerador(idManifiesto,numManifiesto,img, AppDatabase.getFieldName(numManifiesto));
    }

    public void updateFirmaTransportsta(Integer idManifiesto, String numManifiesto, String img){
        actualizarFirmaTrasnportista(idManifiesto,numManifiesto,img, AppDatabase.getFieldName(numManifiesto));
    }

    public void updateFirmaWithPesoTransportista(Integer idManifiesto, Double peso, String nombreFimra, String firmaImg){
        actualizarFirmaWithPesoTransportista(idManifiesto, peso, nombreFimra, firmaImg);
    }

    @Query("Select 10 as code,tecnicoFirmaImg as foto, 10 as tipo, tecnicoFirmaUrl as fotoUrl from tb_manifiestos where idAppManifiesto =:idAppManifiesto")
    public abstract List<ItemFoto> obtenerFotoFirmaTecnicoByIdManifiesto(Integer idAppManifiesto);

    @Query("Select 10 as code, transportistaFirmaImg as foto, 10 as tipo, transportistaFirmaUrl as fotoUrl from tb_manifiestos where idAppManifiesto =:idAppManifiesto")
    public abstract List<ItemFoto> obtenerFotoFirmaTransportistaByIdManifiesto(Integer idAppManifiesto);
    */
    @Query("update tb_manifiestos set idTecnicoGenerador=:idTecnicoGenerador where idAppManifiesto=:idManifiesto")
    public abstract void updateManifiestobyIdManifiesto(Integer idManifiesto, String idTecnicoGenerador);

    @Query("update tb_manifiestos set estado=:estado where idAppManifiesto=:id")
    public abstract void updateManifiestoEstadobyId(Integer id, Integer estado);

    @Query("update tb_manifiestos set correos=:estado where idAppManifiesto=:id")
    public abstract void updateManifiestoCorreos(Integer id, String estado);

    /*
    @Query("update tb_manifiestos set nombreNovedadAudio=:nombreAudio, novedadAudio=:audio,tiempoAudio=:tiempo where idAppManifiesto=:idAppManifiesto")
    public abstract void updateManifiestoNovedadAudio(Integer idAppManifiesto, String nombreAudio,String audio,String tiempo);
    */

    @Query("delete from tb_manifiestos where idAppManifiesto=:idManifiesto")
    abstract void eliminarManifiestobyIdManifiesto(Integer idManifiesto);

    @Query("update tb_manifiestos set numManifiestoCliente=:idManifiestoCliente where idAppManifiesto=:id")
    public abstract void updateManifiestoCliente(Integer id, String idManifiestoCliente);

    @Query("update tb_manifiestos set fechaRecoleccion=:fecha where idAppManifiesto=:idAppManifiesto")
    public abstract void updateManifiestoFechaRecoleccion(Integer idAppManifiesto,Date fecha);

    @Query("update tb_manifiestos set fechaRecepcionPlanta=:fecha where idAppManifiesto=:idAppManifiesto")
    public abstract void updateManifiestoFechaPlanta(Integer idAppManifiesto,Date fecha);

    @Query("update tb_manifiestos set estado=2, sincronizado=1 where idAppManifiesto=:idAppManifiesto ")
    public abstract void updateManifiestoToRecolectado(Integer idAppManifiesto);

    @Query("update tb_manifiestos set estado=3, sincronizado=1 where idAppManifiesto=:idAppManifiesto ")
    public abstract void updateManifiestoToNoRecolectado(Integer idAppManifiesto);

    @Query("update tb_manifiestos set estado=4, sincronizado=1 where idAppManifiesto=:idAppManifiesto ")
    public abstract void updateManifiestoToRecolectadoPlanta(Integer idAppManifiesto);

    @Query("update tb_manifiestos set fechaInicioRecorrecion =:fechaInicioRecoleccion where idAppManifiesto =:idManifiesto")
    public abstract void saveOrUpdateFechaInicioRecoleccion(Integer idManifiesto, Date fechaInicioRecoleccion);

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
            //entity.setIdTecnicoGenerador(manifiesto.getIdTecnicoGenerador());
            entity.setTecnicoResponsable(manifiesto.getTecnicoResponsable());
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setTecnicoCelular(manifiesto.getTecnicoCelular());
            entity.setTecnicoCorreo(manifiesto.getTecnicoCorreo());
            entity.setCorreoAlterno(manifiesto.getCorreoAlterno());
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
            entity.setIdChoferRecolector(manifiesto.getIdChoferRecolector());
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
            entity.setIdTransporteVehiculo(manifiesto.getIdTransporteVehiculo());
            entity.setNumeroPlacaVehiculo(manifiesto.getNumeroPlacaVehiculo());
            //modeloVehiculo
            //idDestinatario
            //identificacionDestinatario
            //razonSocialDestinatario
            //numeroLicenciaAmbientalDestinatario
            entity.setEstado(manifiesto.getEstadoApp());
            entity.setFechaManifiesto(manifiesto.getFechaTemp());
            entity.setTipoPaquete(manifiesto.getTipoPaquete());

            entity.setPeso(manifiesto.getPeso());
            entity.setSincronizado(false);
            //entity.setNombreFirma(manifiesto.getNombreFirma());
            //entity.setFirmaImg(manifiesto.getFirmaImg());
            entity.setIdRuta(manifiesto.getIdRuta());
            entity.setIdSubRuta(manifiesto.getIdSubRuta());
            entity.setIdentificacionOperadorRecolector(manifiesto.getIdentificacionOperadorRecolector());
            entity.setNombreOperadorRecolector(manifiesto.getNombreOperadorRecolector());
            entity.setEstadoFinRuta(manifiesto.getEstadoFinRuta());
            entity.setNombreDestinatario(manifiesto.getNombreDestinatario());
            entity.setIdDestinatarioFinRutaCatalogo(manifiesto.getIdDestinatarioFinRutaCatalogo());
            entity.setApertura1(manifiesto.getApertura1());
            entity.setApertura2(manifiesto.getApertura2());
            entity.setCierre1(manifiesto.getCierre1());
            entity.setCierre2(manifiesto.getCierre2());
            entity.setSerie(manifiesto.getSerie());
            entity.setFrecuencia(manifiesto.getFrecuencia());
            entity.setSucursal(manifiesto.getSucursal());
            entity.setTelefono(manifiesto.getTelefono());

        }else if(entity!=null && !manifiesto.getEliminado() ){

            entity.setNumeroManifiesto(manifiesto.getNumeroManifiesto());
            entity.setNumManifiestoCliente(manifiesto.getNumManifiestoCliente());
            //entity.setIdTecnicoGenerador(manifiesto.getIdTecnicoGenerador());
            entity.setTecnicoResponsable(manifiesto.getTecnicoResponsable());
            entity.setTecnicoTelefono(manifiesto.getTecnicoTelefono());
            entity.setTecnicoCelular(manifiesto.getTecnicoCelular());
            entity.setTecnicoCorreo(manifiesto.getTecnicoCorreo());
            entity.setCorreoAlterno(manifiesto.getCorreoAlterno());
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
            entity.setIdChoferRecolector(manifiesto.getIdChoferRecolector());
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
            entity.setNumeroPlacaVehiculo(manifiesto.getNumeroPlacaVehiculo());
            //modeloVehiculo
            //idDestinatario
            //identificacionDestinatario
            //razonSocialDestinatario
            //numeroLicenciaAmbientalDestinatario
            entity.setEstado(manifiesto.getEstadoApp());
            entity.setFechaManifiesto(manifiesto.getFechaTemp());
            entity.setTipoPaquete(manifiesto.getTipoPaquete());
            entity.setCorreoAlterno(manifiesto.getCorreoAlterno());

            entity.setPeso(manifiesto.getPeso());
            entity.setSincronizado(false);
            //entity.setNombreFirma(manifiesto.getNombreFirma());
            //entity.setFirmaImg(manifiesto.getFirmaImg());
            entity.setIdRuta(manifiesto.getIdRuta());
            entity.setIdSubRuta(manifiesto.getIdSubRuta());
            entity.setIdentificacionOperadorRecolector(manifiesto.getIdentificacionOperadorRecolector());
            entity.setNombreOperadorRecolector(manifiesto.getNombreOperadorRecolector());
            entity.setEstadoFinRuta(manifiesto.getEstadoFinRuta());
            entity.setNombreDestinatario(manifiesto.getNombreDestinatario());
            entity.setIdDestinatarioFinRutaCatalogo(manifiesto.getIdDestinatarioFinRutaCatalogo());
            entity.setApertura1(manifiesto.getApertura1());
            entity.setApertura2(manifiesto.getApertura2());
            entity.setCierre1(manifiesto.getCierre1());
            entity.setCierre2(manifiesto.getCierre2());
            entity.setApertura1(manifiesto.getApertura1());
            entity.setApertura2(manifiesto.getApertura2());
            entity.setCierre1(manifiesto.getCierre1());
            entity.setCierre2(manifiesto.getCierre2());
            entity.setSerie(manifiesto.getSerie());
            entity.setFrecuencia(manifiesto.getFrecuencia());
            entity.setSucursal(manifiesto.getSucursal());
            entity.setTelefono(manifiesto.getTelefono());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
