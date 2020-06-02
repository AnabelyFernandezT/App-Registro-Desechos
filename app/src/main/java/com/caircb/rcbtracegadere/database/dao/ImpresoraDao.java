package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ImpresoraEntity;
import com.caircb.rcbtracegadere.models.RowPrinters;

import java.util.List;

@Dao
 public abstract class ImpresoraDao {

    @Query("SELECT *  FROM tb_impresora")
    public abstract List<RowPrinters> getListaImpresora();

    @Query("SELECT * FROM tb_impresora where address =:address  LIMIT 1")
    abstract ImpresoraEntity searchImpresora(String address );

    @Query("Select address from tb_impresora ")
    public abstract String searchMac();

    @Query("Delete from tb_impresora where name =:address")
    public abstract void deleteImpresoraById(String address);

   @Query("Delete from tb_impresora")
   public abstract void deleteImpresora();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertImpresora(ImpresoraEntity entity);

   public void saveOrUpdateImpresora(String nombre , String mac ){
       ImpresoraEntity r = searchImpresora(mac);
      if(r == null){
         r = new ImpresoraEntity(
                 nombre,
                 mac);

      }
      insertImpresora(r);
   }

}
