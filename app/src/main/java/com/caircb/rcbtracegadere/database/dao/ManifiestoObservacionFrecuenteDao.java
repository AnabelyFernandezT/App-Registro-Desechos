package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoObservacionFrecuenteEntity;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoObservacionFrecuente;

import java.util.List;

@Dao
public abstract class ManifiestoObservacionFrecuenteDao {


    @Query("select * from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idManifiesto")
    public  abstract List<ManifiestoObservacionFrecuenteEntity> fecthConsultarManifiestoObservacionesSeleccionadas(Integer idManifiesto);

    //@Query("update tb_manifiestos_novedad_frecuente set estadoChek=:check where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo")
    //public abstract void updateManifiestoObservacionbyId(Integer idManifiesto, Integer idCatalogo, boolean check);

    @Query("update tb_manifiestos_novedad_frecuente set estadoChekRecepcion=:check where idAppManifiesto=:idManifiesto and  _id=:id")
    public abstract void updateManifiestoObservacionRecepcionbyId(Integer idManifiesto,Integer id, boolean check);

    @Query("select * from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo")
    public abstract ManifiestoObservacionFrecuenteEntity fetchHojaRutaObservacionFrecuentebyCatalogo(Integer idManifiesto, Integer idCatalogo);

    @Query("select * from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo")
    public abstract ManifiestoObservacionFrecuenteEntity fetchHojaRutaObservacionFrecuentebyCatalogoRecepcion(Integer idManifiesto, Integer idCatalogo);

   /* @Query("select c.idSistema as id, upper(c.codigo || '  ' || c.nombre) as catalogo,estadoChek," +
            "(select count(*) from tb_manifiestos_novedad_foto ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=1) as numFotos from tb_manifiestos_novedad_frecuente n " +
            "inner join tb_catalogos c on n.idCatalogo=c.idSistema and n.idAppManifiesto=:idManifiesto and c.tipo=1")
    public  abstract List<RowItemHojaRutaCatalogo> fetchHojaRutaCatalogoNovedaFrecuente(Integer idManifiesto);*/

    @Query("select c.idSistema as id, upper(c.codigo || '  ' || c.nombre) as catalogo," +
            "( select count(*) from tb_manifiestos_novedad_frecuente  nor where nor.idCatalogo=c.idsistema and nor.idAppManifiesto=:idManifiesto limit 1) as estadoChek, " +
            "( select count(*) from tb_manifiestos_file ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=1) as numFotos" +
            " from tb_catalogos c where tipo=1")
    public  abstract List<RowItemHojaRutaCatalogo> fetchHojaRutaCatalogoNovedaFrecuente(Integer idManifiesto);

    @Query("select c.idSistema as id, upper(c.codigo || '  ' || c.nombre) as catalogo," +
            "( select estadoChekRecepcion from tb_manifiestos_novedad_frecuente  nor where nor.idCatalogo=c.idsistema and nor.idAppManifiesto=:idManifiesto limit 1) as estadoChek, " +
            "( select count(*) from tb_manifiestos_file ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=3) as numFotos" +
            " from tb_catalogos c where tipo=1")
    public  abstract List<RowItemHojaRutaCatalogo> fetchHojaRutaCatalogoNovedaFrecuenteRecepcion(Integer idManifiesto);

    @Query("select count(*) " +
            " from tb_catalogos c" +
            " inner join tb_manifiestos_novedad_frecuente mnf on c.idSistema=mnf.idCatalogo and idAppManifiesto=:idManifiesto and c.tipo=1 and estadoChek=1" +
            " where (select count(*) from tb_manifiestos_file ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=1)=0")
    public  abstract long existeNovedadFrecuentePendienteFoto(Integer idManifiesto);

    @Query("select n._id as id, upper(c.nombre) as catalogo,estadoChek, 0 as numFotos from tb_manifiestos_novedad_frecuente n " +
            "inner join tb_catalogos c on n.idCatalogo=c.idSistema and n.idAppManifiesto=:idManifiesto")
    public  abstract List<RowItemHojaRutaCatalogo> fetchReportHojaRutaCatalogo(Integer idManifiesto);


    @Query("select idCatalogo from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idAppManifiesto and estadoChek=1")
    public abstract List<Integer> fetchConsultarNovedadFrecuente(Integer idAppManifiesto);

    /*
    public Cursor fetchHojaRutaCatalogo(Integer idAppManifiesto) throws SQLException {
        Cursor mCursor = database.rawQuery("select mc._id as id ,upper(c.codigo || '  ' || c.nombre) as nombre,c.nombre as nombreObservacion," +
                "(case when (mc.estadoChek =0) then ' ' else 'X' end )as estadoChekObservacion, mc.estadoChek as estadoChek  from catalogo c inner join manifiestoCatalogo mc on c.idSistema = mc.idCatalogoObservacion and idAppManifiesto="+idAppManifiesto+" and c.tipo="+1,null);
        if (mCursor.getCount()>0){
            mCursor.moveToFirst();
        }
        return mCursor;
    }
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoObservacionFrecuente(ManifiestoObservacionFrecuenteEntity entity);

    public void saveOrUpdate(DtoManifiestoObservacionFrecuente novedad){

        ManifiestoObservacionFrecuenteEntity entity =fetchHojaRutaObservacionFrecuentebyCatalogo(novedad.getIdAppManifiesto(),novedad.getIdCatalogoObservacion());
        if(entity==null){
            entity = new ManifiestoObservacionFrecuenteEntity();
            entity.setIdAppManifiesto(novedad.getIdAppManifiesto());
            entity.setIdCatalogo(novedad.getIdCatalogoObservacion());
            entity.setEstadoChek(false);
        }else{
            entity.setEstadoChek(novedad.isEstadoChek());
        }

        createManifiestoObservacionFrecuente(entity);
    }

    public void saveOrUpdateManifiestoNovedadFrecuente(Integer idAppManifiesto, Integer idCatalogo, boolean check){
        ManifiestoObservacionFrecuenteEntity entity =fetchHojaRutaObservacionFrecuentebyCatalogo(idAppManifiesto,idCatalogo);
        if(entity==null){
            entity = new ManifiestoObservacionFrecuenteEntity();
            entity.setIdAppManifiesto(idAppManifiesto);
            entity.setIdCatalogo(idCatalogo);
            entity.setEstadoChek(check);
        }else{
            entity.setEstadoChek(check);
        }
        createManifiestoObservacionFrecuente(entity);
    }

    public void saveOrUpdateManifiestoNovedadFrecuenteRecepcion(Integer idAppManifiesto, Integer idCatalogo, boolean check){
        ManifiestoObservacionFrecuenteEntity entity =fetchHojaRutaObservacionFrecuentebyCatalogoRecepcion(idAppManifiesto,idCatalogo);
        if(entity==null){
            entity = new ManifiestoObservacionFrecuenteEntity();
            entity.setIdAppManifiesto(idAppManifiesto);
            entity.setIdCatalogo(idCatalogo);
            entity.setEstadoChekRecepcion(check);
        }else{
            entity.setEstadoChekRecepcion(check);
        }
        createManifiestoObservacionFrecuente(entity);
    }
}
