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
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;
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

    @Query("select idManifiestoPadre,numeroManifiestoPadre from tb_lotes_padre")
    @Transaction
    public abstract List<ItemLotePadre> fetchLote();

    @Query("select * from tb_lotes_padre where idManifiestoPadre=:tipo")
    public abstract LotePadreEntity fetchConsultarCatalogoEspecifico(Integer tipo);

    @Query("Select *from tb_lotes_padre where idManifiestoDetalleHijo=:idManifiestoDetalleHijo")
    public abstract LotePadreEntity fetchDetalleHijos(Integer idManifiestoDetalleHijo);

    @Query("UPDATE tb_lotes_padre set checkHijo=:check WHERE idManifiestoDetalleHijo=:idManifiestoDetalleHijo")
    public abstract void updateCheck(Integer idManifiestoDetalleHijo, boolean check);

    @Query("SELECT * FROM tb_lotes_padre WHERE idDesecho=:idDesecho and bultos>0")
    public abstract List<RowItemManifiestosDetalleGestores> fetchManifiestosRecolectadosByDetalle(Integer idDesecho);

    @Query("UPDATE tb_lotes_padre set idManifiestoPadre=:idManifiestoPadre, idManifiestoDetallePadre=:idManifiestoDetallePadre, numeroManifiestoPadre=:numeroManifiestoPadre WHERE idDesecho =:idDesecho")
    public abstract void asociarManifiestoPadre(Integer idManifiestoPadre, Integer idManifiestoDetallePadre, String numeroManifiestoPadre,Integer idDesecho);

    @Query("select * from tb_lotes_padre where idManifiestoPadre=:idManifiesto and checkHijo=1" )
    public abstract List<LotePadreEntity> manifiestoPadreHijos(Integer idManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createLote(LotePadreEntity entity);

    public void saveOrUpdate(List<RowItemManifiestosDetalleGestores> manifiestoPadre){
        for(RowItemManifiestosDetalleGestores detalle:manifiestoPadre){
            LotePadreEntity lote = fetchDetalleHijos(detalle.getIdManifiestoDetalleHijo());
            if(lote==null){
                lote = new LotePadreEntity();
                lote.setIdManifiestosHijo(detalle.getIdManifiestosHijo());
                lote.setIdManifiestoDetalleHijo(detalle.getIdManifiestoDetalleHijo());
                lote.setIdDesecho(detalle.getIdDesecho());
                lote.setPeso(detalle.getPeso());
                lote.setBultos(detalle.getBultos());
                lote.setNumeroManifiestoHijo(detalle.getNumeroManifiestoHijo());
                lote.setCliente(detalle.getCliente());
                lote.setCheckHijo(false);
            }else{

            }
            createLote(lote);
        }
    }


    public void saveOrUpdate(DtoLotePadreGestor c){
        LotePadreEntity catalogo = fetchConsultarCatalogoEspecifico(c.getIdManifiestoPadre());

        if (catalogo == null) {
            catalogo = new LotePadreEntity();
            catalogo.setIdManifiestoPadre(c.getIdManifiestoPadre());
           // catalogo.setManifiestos(c.getManifiestos());
            //catalogo.setTotal(c.getTotal());
            //catalogo.setNombreCliente(c.getNombreCliente());
            catalogo.setNumeroManifiestoPadre(c.getNumeroManifiestoPadre());
           // catalogo.setPlacaVehiculo(c.getPlacaVehiculo());

        } if(catalogo!=null  ) {
            catalogo = new LotePadreEntity();
            catalogo.setIdManifiestoPadre(c.getIdManifiestoPadre());
           // catalogo.setManifiestos(c.getManifiestos());
           // catalogo.setTotal(c.getTotal());
           // catalogo.setNombreCliente(c.getNombreCliente());
            catalogo.setNumeroManifiestoPadre(c.getNumeroManifiestoPadre());
           // catalogo.setPlacaVehiculo(c.getPlacaVehiculo());
        }
        if (catalogo!=null) createLote(catalogo);
    }

}
