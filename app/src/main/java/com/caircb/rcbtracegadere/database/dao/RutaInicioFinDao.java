package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;

import java.util.Date;
import java.util.List;

@Dao
public abstract class RutaInicioFinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createRegistro(RutaInicioFinEntity entity);

    public void saveOrUpdateInicioRuta(Integer idRutaInicioFin, Integer idTransporteRecolector, Integer IdTransporteVehiculo, Date fechaInicio, Date fechaFin, String kilometrajeInicio, String kilometrajeFin, int estado ){
        RutaInicioFinEntity registroInicio = fechConsultaInicioFinRutasE(idTransporteRecolector);
        if(registroInicio==null) {
            registroInicio = new RutaInicioFinEntity(idRutaInicioFin,idTransporteRecolector,IdTransporteVehiculo,fechaInicio,fechaFin,kilometrajeInicio,kilometrajeFin,estado);
        }else{
            registroInicio.setIdRutaInicioFin(idRutaInicioFin);
            registroInicio.setFechaFin(fechaFin);
            registroInicio.setKilometrajeFin(kilometrajeFin);
            registroInicio.setEstado(estado);

        }

        createRegistro(registroInicio);
    }


    @Query("select * from tb_rutaInicioFin where idTransporteRecolector=:idTransporteRecolector")
    public abstract List<RutaInicioFinEntity> fechConsultaInicioFinRutas(Integer idTransporteRecolector );

    @Query("select * from tb_rutaInicioFin where idTransporteRecolector=:idTransporteRecolector limit 1")
    public abstract RutaInicioFinEntity fechConsultaInicioFinRutasE(Integer idTransporteRecolector );


}
