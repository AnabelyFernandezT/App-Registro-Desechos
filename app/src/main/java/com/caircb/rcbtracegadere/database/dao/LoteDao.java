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
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;

import java.util.Date;
import java.util.List;

@Dao
public abstract  class LoteDao {

    @Query("select * from tb_lotes")
    public abstract LoteEntity fetchLotesCompletos();

    @Query("select * from tb_lotes where idLoteContenedor=:loteContenedor")
    public abstract LoteEntity fetchLotesCompletosE(Integer loteContenedor);

    @Query("select idLoteContenedor,codigoLote,fechaRegistro, idDestinatarioFinRutaCatalogo, nombreDestinatarioFinRutaCatalogo,numeroManifiesto,subRuta,ruta,placaVehiculo from tb_lotes")
    @Transaction
    public abstract List<ItemLote> fetchLote();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createLote(LoteEntity entity);

    public void saveOrUpdate(DtoLote lotes){
        LoteEntity lote = fetchLotesCompletosE(lotes.getIdLoteContenedor());

            if (lote==null) {
                lote = new LoteEntity();
                lote.setIdLoteContenedor(lotes.getIdLoteContenedor());
                lote.setCodigoLote(lotes.getCodigoLote());
                lote.setFechaRegistro(lotes.getFechaRegistro());
                lote.setIdDestinatarioFinRutaCatalogo(lotes.getIdDestinatarioFinRutaCatalogo());
                lote.setNombreDestinatarioFinRutaCatalogo(lotes.getNombreDestinatarioFinRutaCatalogo());
                lote.setNumeroManifiesto(lotes.getNumeroManifiesto());
                lote.setSubRuta(lotes.getSubRuta());
                lote.setRuta(lotes.getRuta());
                lote.setPlacaVehiculo(lotes.getPlacaVehiculo());
            } else {
                lote.setCodigoLote(lotes.getCodigoLote());
                lote.setIdLoteContenedor(lotes.getIdLoteContenedor());
                lote.setFechaRegistro(lotes.getFechaRegistro());
                lote.setIdDestinatarioFinRutaCatalogo(lotes.getIdDestinatarioFinRutaCatalogo());
                lote.setNombreDestinatarioFinRutaCatalogo(lotes.getNombreDestinatarioFinRutaCatalogo());
                lote.setNumeroManifiesto(lotes.getNumeroManifiesto());
                lote.setSubRuta(lotes.getSubRuta());
                lote.setRuta(lotes.getRuta());
                lote.setPlacaVehiculo(lotes.getPlacaVehiculo());
            }

            createLote(lote);
        }


}
