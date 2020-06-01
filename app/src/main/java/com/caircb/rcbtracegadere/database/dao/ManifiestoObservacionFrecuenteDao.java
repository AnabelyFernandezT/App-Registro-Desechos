package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoObservacionFrecuenteEntity;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoObservacionFrecuente;

import java.util.List;

@Dao
public abstract class ManifiestoObservacionFrecuenteDao {


    @Query("select * from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idManifiesto")
    public  abstract List<ManifiestoObservacionFrecuenteEntity> fecthConsultarManifiestoObservacionesSeleccionadas(Integer idManifiesto);

    @Query("update tb_manifiestos_novedad_frecuente set estadoChek=:check where _id=:id")
    public abstract void updateManifiestoObservacionbyId(Integer id, boolean check);

    @Query("select * from tb_manifiestos_novedad_frecuente where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo")
    public abstract ManifiestoObservacionFrecuenteEntity fetchHojaRutaObservacionFrecuentebyCatalogo(Integer idManifiesto, Integer idCatalogo);

    @Query("select c.idSistema as id, upper(c.codigo || '  ' || c.nombre) as catalogo,estadoChek," +
            "(select count(*) from tb_manifiestos_novedad_foto ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=1) as numFotos from tb_manifiestos_novedad_frecuente n " +
            "inner join tb_catalogos c on n.idCatalogo=c.idSistema and n.idAppManifiesto=:idManifiesto and c.tipo=1")
    public  abstract List<RowItemHojaRutaCatalogo> fetchHojaRutaCatalogoNovedaFrecuente(Integer idManifiesto);

    @Query("select n._id as id, upper(c.nombre) as catalogo,estadoChek, 0 as numFotos from tb_manifiestos_novedad_frecuente n " +
            "inner join tb_catalogos c on n.idCatalogo=c.idSistema and n.idAppManifiesto=:idManifiesto")
    public  abstract List<RowItemHojaRutaCatalogo> fetchReportHojaRutaCatalogo(Integer idManifiesto);


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
}
