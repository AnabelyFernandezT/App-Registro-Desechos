package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;

import java.util.List;

@Dao
public abstract class ManifiestoDetallePesosDao {


    @Query("select _id as idCatalogo, valor,descripcion as tipo from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle ")
    public abstract List<CatalogoItemValor> fecthConsultarValores(Integer idManifiesto, Integer idManifiestoDetalle);

    //@Query("select sum(valor) as suma from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle")
    //public abstract Double fechConsultaSumaValores(Integer idManifiesto, Integer idManifiestoDetalle);

    @Query("delete from tb_manifiesto_detalle_pesos")
    public abstract void deleteTableValores();

    @Query("delete from tb_manifiesto_detalle_pesos where _id=:id")
    public abstract void deleteTableValoresById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insertValores(ManifiestoDetallePesosEntity entity);

    public long saveValores (int idManifiesto, int idManifiestoDetalle, double valor, String descricpion){
        ManifiestoDetallePesosEntity r = new ManifiestoDetallePesosEntity(valor,idManifiesto,idManifiestoDetalle,descricpion);
        return insertValores(r);
    }
}