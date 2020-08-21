package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.NotificacionPesoExtraEntity;


@Dao
public abstract class NotificacionPesoExtraDao {

    @Query("select * from tb_notificacion_peso_extra where idManifiesto=:idManifiesto and idManifiestoDetalle=:idManifiestoDetalle")
    public abstract NotificacionPesoExtraEntity fetchPesoExtra(Integer idManifiesto, Integer idManifiestoDetalle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createNotificacion(NotificacionPesoExtraEntity entity);

    public void saveOrUpdate(Integer idManifiesto, Integer idManifiestoDetalle , Double pesoAutorizado, Integer autorizacion){
        NotificacionPesoExtraEntity notificacion = fetchPesoExtra(idManifiesto,idManifiestoDetalle);
        if (notificacion==null){
            notificacion = new NotificacionPesoExtraEntity(idManifiesto,idManifiestoDetalle,autorizacion);
        }else {
            notificacion.setNuevoPesoReferencial(pesoAutorizado);
            notificacion.setAutorizacion(autorizacion);
        }
        createNotificacion(notificacion);

    }

}
