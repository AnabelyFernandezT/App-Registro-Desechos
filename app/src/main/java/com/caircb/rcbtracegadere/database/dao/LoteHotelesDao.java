package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.LoteHotelesEntity;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.ItemLoteHoteles;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.models.response.DtoLotesHoteles;

import java.util.List;

@Dao
public abstract  class LoteHotelesDao {

    @Query("select * from tb_lotes_hoteles")
    public abstract LoteHotelesEntity fetchLotesCompletos();

    @Query("delete from tb_lotes_hoteles ")
    public abstract void eliminarLotes();

    @Query("select codigoLoteContenedorHotel,codigoLoteContenedor,idLoteContenedor,ruta,subRuta,placaVehiculo,operador, hoteles,chofer from tb_lotes_hoteles where movilizado=0")
    @Transaction
    public abstract List<ItemLoteHoteles> fetchLotesAsigando();


    @Query("select codigoLoteContenedorHotel,codigoLoteContenedor,idLoteContenedor, ruta, " +
            "subRuta,placaVehiculo,operador from tb_lotes_hoteles")
    @Transaction
    public abstract List<ItemLoteHoteles> fetchLote();

    @Query("update tb_lotes_hoteles set movilizado=1 where idLoteContenedor=:id")
    abstract void actualizarMovilizado (Integer id);

    public void updataMovilizado(Integer id){
        actualizarMovilizado(id);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createLote(LoteHotelesEntity entity);

    public void saveOrUpdate(List<DtoLotesHoteles> lotes){
        for(DtoLotesHoteles c:lotes) {
            LoteHotelesEntity lote = fetchLotesCompletos();
            if (lote == null) {
                lote = new LoteHotelesEntity();
                lote.setCodigoLoteContenedorHotel(c.getCodigoLoteContenedorHotel());
                lote.setCodigoLoteContenedor(c.getCodigoLoteContenedor());
                lote.setIdLoteContenedor(c.getIdLoteContenedor());
                lote.setRuta(c.getRuta());
                lote.setSubRuta(c.getSubRuta());
                lote.setPlacaVehiculo(c.getPlacaVehiculo());
                lote.setOperador(c.getOperador());
                lote.setChofer(c.getChofer());
                lote.setHoteles(c.getHoteles());
                lote.setMovilizado(0);
            } else {
                lote.setCodigoLoteContenedorHotel(c.getCodigoLoteContenedorHotel());
                lote.setCodigoLoteContenedor(c.getCodigoLoteContenedor());
                lote.setIdLoteContenedor(c.getIdLoteContenedor());
                lote.setRuta(c.getRuta());
                lote.setSubRuta(c.getSubRuta());
                lote.setPlacaVehiculo(c.getPlacaVehiculo());
                lote.setOperador(c.getOperador());
                lote.setChofer(c.getChofer());
                lote.setHoteles(c.getHoteles());
            }
            createLote(lote);
        }
    }

}
