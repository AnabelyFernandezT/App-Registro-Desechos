package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoMotivoNoRecoleccionEntity;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;

import java.util.List;

@Dao
public abstract class ManifiestoMotivosNoRecoleccionDao {


    @Query("select c.idSistema as id, c.nombre as catalogo," +
            "( select count(*) from tb_manifiestos_motivo_norecoleccion  nor where nor.estadoChek=1 and nor.idCatalogo=c.idsistema and nor.idAppManifiesto=:idManifiesto limit 1) as estadoChek, " +
            "( select count(*) from tb_manifiestos_file ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=2) as numFotos" +
            " from tb_catalogos c where tipo=6")
    public  abstract List<RowItemNoRecoleccion> fetchHojaRutaMotivoNoRecoleccion(Integer idManifiesto);

    @Query("select count(idAppManifiesto) as estadoChek from tb_manifiestos_motivo_norecoleccion  nor " +
            "where nor.estadoChek=1 and nor.idAppManifiesto=:idManifiesto " )
    public  abstract Integer fetchHojaRutaMotivoNoRecoleccionEstado(Integer idManifiesto);

    @Query("select count(*) " +
            " from tb_catalogos c" +
            " inner join tb_manifiestos_motivo_norecoleccion mnf on c.idSistema=mnf.idCatalogo and idAppManifiesto=:idManifiesto and c.tipo=6 and estadoChek=1 and codigo=1" +
            " where (select count(*) from tb_manifiestos_file ff where ff.idAppManifiesto=:idManifiesto and ff.idCatalogo=c.idSistema and ff.tipo=2)=0")
    public  abstract long existeNovedadNoRecoleccionPendienteFoto(Integer idManifiesto);

    @Query("select count(*) " +
            " from tb_catalogos c" +
            " inner join tb_manifiestos_motivo_norecoleccion mnf on c.idSistema=mnf.idCatalogo and idAppManifiesto=:idManifiesto and c.tipo=6 and estadoChek=1 limit 1")
    public  abstract Boolean existeNovedadNoRecoleccion(Integer idManifiesto);

    @Query("update tb_manifiestos_motivo_norecoleccion set estadoChek =:estado where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo ")
    public abstract void updateManifiestoNorecolecion(Integer idManifiesto, Integer idCatalogo, boolean estado);

    @Query("select * from tb_manifiestos_motivo_norecoleccion where idAppManifiesto=:idManifiesto and idCatalogo=:idCatalogo limit 1")
    abstract ManifiestoMotivoNoRecoleccionEntity obtenerMotivoRecoleccion(Integer idManifiesto, Integer idCatalogo);

    @Query("select idCatalogo from tb_manifiestos_motivo_norecoleccion where idAppManifiesto=:idAppManifiesto and estadoChek=1")
    public abstract List<Integer> fetchConsultarMotivoNoRecoleccion(Integer idAppManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createManifiestoMotivoNoRecoleccion(ManifiestoMotivoNoRecoleccionEntity entity);

    public void saveOrUpdate(Integer idManifiesto, Integer idCatalogo, boolean check){
        ManifiestoMotivoNoRecoleccionEntity r = obtenerMotivoRecoleccion(idManifiesto,idCatalogo);
        if(r==null){
            r = new ManifiestoMotivoNoRecoleccionEntity();
            r.setEstadoChek(check);
            r.setIdAppManifiesto(idManifiesto);
            r.setIdCatalogo(idCatalogo);
        }else{
            r.setEstadoChek(check);
        }
        createManifiestoMotivoNoRecoleccion(r);

    }
}
