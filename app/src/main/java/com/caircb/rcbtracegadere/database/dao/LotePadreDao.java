package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.LoteHotelesEntity;
import com.caircb.rcbtracegadere.database.entity.LotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.ItemLotePadre;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreGestor;

import java.util.List;

@Dao
public abstract  class LotePadreDao {

    @Query("select * from tb_lotes_padre")
    public abstract LotePadreEntity fetchLotesCompletos();

    @Query("delete from tb_lotes_padre ")
    public abstract void eliminarLotes();

    @Query("select idManifiestoPadre,manifiestos,total,nombreCliente,numeroManifiestoPadre from tb_lotes_padre")
    @Transaction
    public abstract List<ItemLotePadre> fetchLote();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createLote(LotePadreEntity entity);

    @Query("select * from tb_lotes_padre where idManifiestoPadre=:tipo")
    public abstract LotePadreEntity fetchConsultarCatalogoEspecifico(Integer tipo);


    public void saveOrUpdate(DtoLotePadreGestor c){
        LotePadreEntity catalogo = fetchConsultarCatalogoEspecifico(c.getIdManifiestoPadre());

        if (catalogo == null) {
            catalogo = new LotePadreEntity();
            catalogo.setIdManifiestoPadre(c.getIdManifiestoPadre());
            catalogo.setManifiestos(c.getManifiestos());
            catalogo.setTotal(c.getTotal());
            catalogo.setNombreCliente(c.getNombreCliente());
            catalogo.setNumeroManifiestoPadre(c.getNumeroManifiestoPadre());

        } if(catalogo!=null  ) {
            catalogo = new LotePadreEntity();
            catalogo.setIdManifiestoPadre(c.getIdManifiestoPadre());
            catalogo.setManifiestos(c.getManifiestos());
            catalogo.setTotal(c.getTotal());
            catalogo.setNombreCliente(c.getNombreCliente());
            catalogo.setNumeroManifiestoPadre(c.getNumeroManifiestoPadre());

        }
        if (catalogo!=null) createLote(catalogo);
    }

}
