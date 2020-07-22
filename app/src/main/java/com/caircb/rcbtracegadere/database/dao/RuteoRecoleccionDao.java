package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;

import java.util.Date;
import java.util.List;

@Dao
public abstract  class RuteoRecoleccionDao {

    @Query("select * from tb_ruteo_recoleccion")
    public abstract List<RuteoRecoleccionEntity> searchRuteoRecoleccion();

    @Query("Select _id from tb_ruteo_recoleccion where puntoLlegada=:puntoLlegada and estado=0 limit 1")
    public abstract Integer searchRegistroLlegada (Integer puntoLlegada);

    @Query("update tb_ruteo_recoleccion set estado = 1 where  puntoLlegada=:puntoLlegada and _id=:id ")
    public abstract void updateEstadoByPuntoLLegada(Integer id ,Integer puntoLlegada);

    @Query("select * from tb_ruteo_recoleccion where _id=:id and puntoLlegada=:puntoLlegada limit 1")
    public abstract RuteoRecoleccionEntity dtoSendServicio (Integer id, Integer puntoLlegada);

    @Query("Select estado from tb_ruteo_recoleccion where puntoPartida=:puntoPartida limit 1")
    public abstract boolean verificaEstadoPrimerRegistro (Integer puntoPartida);

    @Query("Select _id from tb_ruteo_recoleccion where puntoPartida=:puntoPartida limit 1")
    public abstract Integer selectIdByPuntoPartida (Integer puntoPartida);

    @Query("Select _id from tb_ruteo_recoleccion where puntoPartida > 0 and estado = 0 order by _id desc limit 1")
    public abstract Integer searchRegistroPuntodePartidaMayorACero();

    @Query("update tb_ruteo_recoleccion set puntoLlegada = null, fechaLlegadaRuta = null  where puntoPartida > 0 and estado = 0 and _id=:id")
    public abstract void setPuntoLlegadaAndFecha(Integer id);

    @Query("update tb_ruteo_recoleccion set puntoLlegada=:puntoLlegada, fechaLlegadaRuta=:fechaLlegadaRuta where _id =:id")
    public abstract void updatePrimerRegistroRuteoRecoleccion (Integer id, Integer puntoLlegada, Date fechaLlegadaRuta);

    @Query("select * from tb_ruteo_recoleccion where puntoPartida =:puntoPartida and estado=0 limit 1")
    public abstract RuteoRecoleccionEntity fectchConsultarRutaById(Integer puntoPartida);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createRuteoRecoleccion(RuteoRecoleccionEntity entity);

    public void saverOrUpdate(DtoRuteoRecoleccion dto){
        RuteoRecoleccionEntity ruteo;

        ruteo = fectchConsultarRutaById(dto.getPuntoPartida());
        if(ruteo == null){
            ruteo = new RuteoRecoleccionEntity();
            ruteo.setIdSubRuta(dto.getIdSubRuta());
            ruteo.setFechaInicioRuta(dto.getFechaInicioRuta());
            ruteo.setPuntoPartida(dto.getPuntoPartida());
            ruteo.setPuntoLlegada(dto.getPuntoLlegada());
            ruteo.setFechaLlegadaRuta(dto.getFechaLlegadaRuta());
            ruteo.setEstado(dto.isEstado());
        }else{

            ruteo.setIdSubRuta(dto.getIdSubRuta());
            ruteo.setFechaInicioRuta(dto.getFechaInicioRuta());
            ruteo.setPuntoPartida(dto.getPuntoPartida());
            ruteo.setPuntoLlegada(dto.getPuntoLlegada());
            ruteo.setFechaLlegadaRuta(dto.getFechaLlegadaRuta());
            ruteo.setEstado(dto.isEstado());
        }

        createRuteoRecoleccion(ruteo);
    }
}
