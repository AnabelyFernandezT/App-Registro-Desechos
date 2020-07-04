package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ManifiestoSedeDetalleValorDao {


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero, estado from tb_manifiestos where estado=2 order by nombreCliente")
    public abstract List<RowItemHojaRuta> fetchHojaRutaPickIn();

    @Query("select * from tb_manifiestos_sede_det_valor where idManifiestoHijo=:idManifiesto limit 1")
    public abstract ManifiestoSedeDetalleValorEntity fetchHojaRutabyIdManifiesto(Integer idManifiesto);

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

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1000 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosBuscarData();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado in(2,3) order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosProcesados();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=1 and idTransporteVehiculo =:idPlaca order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandoByPlaca(Integer idPlaca);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos where estado=2 order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigandoPlanta();

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where estado=1 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManif(String search);

    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
    @Transaction
    public abstract List<ItemManifiesto> fetchManifiestosAsigByClienteOrNumManifSearch(String search);


    @Query("select idAppManifiesto,nombreCliente as cliente,numeroManifiesto as numero,'' as sucursal, direccionCliente as direccion,provincia as provincia, canton as canton, estado from tb_manifiestos " +
            "where estado=2 and (numeroManifiesto like '%' || :search || '%' or nombreCliente like '%' || :search || '%')  order by nombreCliente")
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiesto(ManifiestoSedeDetalleValorEntity entity);

    public void saveOrUpdate(DtoManifiestoDetalleValorSede manifiesto){

        ManifiestoSedeDetalleValorEntity entity;

        entity = fetchHojaRutabyIdManifiesto(manifiesto.getIdManifiestoDetalle());
        if(entity==null){
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setIdManifiestoDetalleValores(manifiesto.getIdManifiestoDetalle());
            entity.setIdManifiestoHijo(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());

        }else if(entity!=null  ){
            entity = new ManifiestoSedeDetalleValorEntity();
            entity.setCodigoQR(manifiesto.getCodigoQR());
            entity.setIdManifiestoDetalleValores(manifiesto.getIdManifiestoDetalle());
            entity.setIdManifiestoHijo(manifiesto.getIdManifiestoDetalle());
            entity.setPeso(manifiesto.getPeso());
        }

        if (entity!=null) createManifiesto(entity);
    }



}
