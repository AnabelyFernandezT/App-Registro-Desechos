package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ImpresoraEntity;
import com.caircb.rcbtracegadere.models.ItemGeneric;
import com.caircb.rcbtracegadere.models.RowPrinters;
import com.caircb.rcbtracegadere.models.response.DtoImpresora;

import java.util.List;

@Dao
 public abstract class ImpresoraDao {

    @Query("SELECT code,address  FROM tb_impresora")
    public abstract List<RowPrinters> getListaImpresora();

    @Query("select count(*) from tb_impresora where useActive=1 and type=:tipo limit 1")
    public abstract Boolean existeImpresora(Integer tipo);

    @Query("select count(*) from tb_impresora limit 1")
    public abstract Boolean existeCatalogoImpresora();

    @Query("SELECT * FROM tb_impresora where printID =:uuid  LIMIT 1")
    abstract ImpresoraEntity searchImpresora(String uuid );

    @Query("SELECT _id as id, code as nombre FROM tb_impresora where type =:tipo")
    public abstract List<ItemGeneric> searchListImpresorabyTipoRuta(Integer tipo);

    @Query("Select address from tb_impresora where  useActive=1 and type=:tipo limit 1")
    public abstract String searchMac(Integer tipo);

    @Query("Select _id as id, code as nombre from tb_impresora where printID=:uuid limit 1")
    public abstract ItemGeneric searchCodigoUUID(String uuid);

    @Query("Delete from tb_impresora where printID =:uuid")
    public abstract void deleteImpresoraById(String uuid);

   @Query("Delete from tb_impresora")
   public abstract void deleteImpresora();

    @Query("update tb_impresora set useActive=1  where _id=:id")
    public abstract void updateDefaulImpresoraWorked(Integer id);

    @Query("update tb_impresora set useActive=0")
    public abstract void updateDisabledAllImpresoraWorked();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertImpresora(ImpresoraEntity entity);

   public void saveOrUpdateImpresora(DtoImpresora impresora){
       ImpresoraEntity r = searchImpresora(impresora.getId());
       if(!impresora.isEliminado()) {
           if (r == null) {
               r = new ImpresoraEntity(
                       impresora.getId(),
                       impresora.getCodigo(),
                       impresora.getDireccionmac(),
                       impresora.getTipo(),
                       false
               );

           } else {
                r.setCode(impresora.getCodigo());
                r.setType(impresora.getTipo());
           }
           insertImpresora(r);
       }else{
           //eliminar impresora si existe
           if(r!=null)deleteImpresoraById(impresora.getId());
       }
   }

}
