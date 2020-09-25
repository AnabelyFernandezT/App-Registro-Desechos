package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.NotificacionEntity;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.ItemNotificacion;
import com.caircb.rcbtracegadere.models.response.DtoLote;

import java.util.List;

@Dao
public abstract  class NotificacionDao {

    @Query("select * from tb_notificaciones")
    public abstract List<NotificacionEntity>  fetchNotificaciones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createNotificacion(NotificacionEntity notificacionEntity);

    @Query("Delete from tb_notificaciones where idNotificacion = :idNotificacion")
    public abstract void deleteNotification(Integer idNotificacion);

    @Query("Delete from tb_notificaciones where tipoNotificacion = :tipoNotificacion")
    public abstract void deleteNotificationTipoCierreLote(String tipoNotificacion);

    public void saveOrUpdate(ItemNotificacion notificacion){
        NotificacionEntity newNotificacion;
        newNotificacion = new NotificacionEntity();
        newNotificacion.setEstadoNotificacion(notificacion.getEstadoNotificacion());
        newNotificacion.setNombreNotificacion(notificacion.getNombreNotificacion());
        newNotificacion.setTipoNotificacion(notificacion.getTipoNotificacion());
        newNotificacion.setIdManifiesto(notificacion.getIdManifiesto());
        newNotificacion.setPeso(notificacion.getPeso());
        createNotificacion(newNotificacion);
        /*/System.out.println("Lista notificaciones");
        System.out.println(newNotificacion.getEstadoNotificacion());
        System.out.println(newNotificacion.getIdManifiesto());
        System.out.println(newNotificacion.getIdNotificacion());
        System.out.println(newNotificacion.getNombreNotificacion());
        System.out.println(newNotificacion.getPeso());
        System.out.println(newNotificacion.getTipoNotificacion());


        System.out.println(fetchNotificaciones());*/
        }


}
