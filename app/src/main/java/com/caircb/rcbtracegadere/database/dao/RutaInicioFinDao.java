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


    @Query("update tb_rutaInicioFin set sincronizado=1,idRutaInicioFin=:idSistema where _id=:idRegistro")
    public abstract void actualizarInicioFinRutaToSincronizado(Integer idRegistro,Integer idSistema);

    @Query("update tb_rutaInicioFin set sincronizadoFin=1 where _id=:idRegistro")
    public abstract void actualizarFinRutaToSincronizado(Integer idRegistro);

    @Query("update tb_rutaInicioFin set kilometrajeInicio=:nuevoKilometraje where idTransporteRecolector=:idRecolector")
    public abstract void actualizarKilometraje(Integer idRecolector, String nuevoKilometraje);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long createRegistro(RutaInicioFinEntity entity);

    public long saveOrUpdateInicioRuta(Integer idRutaInicioFin, Integer idTransporteRecolector, Integer IdTransporteVehiculo, Date fechaInicio, Date fechaFin, String kilometrajeInicio, String kilometrajeFin, int estado, int tiposubruta ){
        RutaInicioFinEntity registroInicio = fechConsultaInicioFinRutas(idTransporteRecolector);
        if(registroInicio==null) {
            registroInicio = new RutaInicioFinEntity(
                    idRutaInicioFin,
                    idTransporteRecolector,
                    IdTransporteVehiculo, //idSubruta
                    fechaInicio,
                    fechaFin,
                    kilometrajeInicio,
                    kilometrajeFin,
                    estado,
                    tiposubruta);
        }else{
            registroInicio.setIdRutaInicioFin(idRutaInicioFin);
            registroInicio.setFechaFin(fechaFin);
            registroInicio.setKilometrajeFin(kilometrajeFin);
            registroInicio.setEstado(estado);
            registroInicio.setTiposubruta(tiposubruta);

        }

        return createRegistro(registroInicio);
    }

   /* public long saveOrUpdateFijRuta(Integer idRutaInicioFin, Integer idTransporteRecolector, Integer IdTransporteVehiculo, Date fechaInicio, Date fechaFin, String kilometrajeInicio, String kilometrajeFin, int estado ){
        RutaInicioFinEntity registroInicio = fechConsultaInicioFinRutasE(idTransporteRecolector);
        if(registroInicio==null) {
            registroInicio = new RutaInicioFinEntity(
                    idRutaInicioFin,
                    idTransporteRecolector,
                    IdTransporteVehiculo, //idSubruta
                    fechaInicio,
                    fechaFin,
                    kilometrajeInicio,
                    kilometrajeFin);
        }else{
            registroInicio.setIdRutaInicioFin(idRutaInicioFin);
            registroInicio.setFechaFin(fechaFin);
            registroInicio.setKilometrajeFin(kilometrajeFin);
            registroInicio.setEstado(estado);

        }

        return createRegistro(registroInicio);
    }*/



    @Query("select * from tb_rutaInicioFin where idTransporteRecolector=:idTransporteRecolector limit 1")
    public abstract RutaInicioFinEntity fechConsultaInicioFinRutas(Integer idTransporteRecolector );

    @Query("select * from tb_rutaInicioFin where idSubRuta=:idSubruta limit 1")
    public abstract RutaInicioFinEntity fechConsultaInicioFinRutasbySubRuta(Integer idSubruta );

    @Query("select * from tb_rutaInicioFin where idTransporteRecolector=:idTransporteRecolector limit 1")
    public abstract RutaInicioFinEntity fechConsultaInicioFinRutasE(Integer idTransporteRecolector );

    @Query("delete from tb_rutaInicioFin ")
    public abstract void eliminarInicioFin();



}
