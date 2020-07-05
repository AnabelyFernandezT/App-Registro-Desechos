package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto; //OJO FALTA CREAR

import java.util.Date;
import java.util.List;

@Dao
public abstract  class LoteDao {

    @Query("select * from tb_lotes")
    public abstract LoteEntity fetchLotesCompletos();

    @Query("select idLoteContenedor,codigoLote,fechaRegistro, idDestinatarioFinRutaCatalogo, nombreDestinatarioFinRutaCatalogo,numeroManifiesto,subRuta,ruta,placaVehiculo from tb_lotes")
    @Transaction
    public abstract List<ItemLote> fetchLote();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createLote(LoteEntity entity);

    public void saveOrUpdate(List<DtoLote> lotes){
        for(DtoLote c:lotes) {
            LoteEntity lote = fetchLotesCompletos();
            if (lote == null) {
                lote = new LoteEntity();
            } else {
                lote.setCodigoLote(c.getCodigoLote());
                lote.setFechaRegistro(c.getFechaRegistro());
                lote.setIdDestinatarioFinRutaCatalogo(c.getIdDestinatarioFinRutaCatalogo());
                lote.setNombreDestinatarioFinRutaCatalogo(c.getNombreDestinatarioFinRutaCatalogo());
                lote.setNumeroManifiesto(c.getNumeroManifiesto());
                lote.setSubRuta(c.getSubRuta());
                lote.setRuta(c.getRuta());
                lote.setPlacaVehiculo(c.getPlacaVehiculo());
            }

            createLote(lote);
        }
    }

}
