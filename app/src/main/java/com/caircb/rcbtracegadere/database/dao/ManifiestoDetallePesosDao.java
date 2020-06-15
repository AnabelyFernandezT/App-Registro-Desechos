package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.ItemEtiqueta;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDetBultos;

import java.util.List;

@Dao
public abstract class ManifiestoDetallePesosDao {


    @Query("select _id as idCatalogo, valor,descripcion as tipo from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle ")
    public abstract List<CatalogoItemValor> fecthConsultarValores(Integer idManifiesto, Integer idManifiestoDetalle);

    @Query("select count(*) from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle and descripcion=:categoria limit 1")
    public abstract boolean existeBultoCategoriaPaquete(Integer idManifiesto, Integer idManifiestoDetalle,String categoria);

    @Query("select * from tb_manifiesto_detalle_pesos where idAppManifiestoDetalle=:idManifiestoDetalle")
    public abstract List<ManifiestoDetallePesosEntity> fecthConsultarBultosManifiestoDet(Integer idManifiestoDetalle);

    @Query("select * from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiestoDetalle")
    public abstract List<ManifiestoDetallePesosEntity> fecthConsultarBultosManifiesto(Integer idManifiestoDetalle);

    @Query("delete from tb_manifiesto_detalle_pesos")
    public abstract void deleteTableValores();

    @Query("delete from tb_manifiesto_detalle_pesos where _id=:id")
    public abstract void deleteTableValoresById(Integer id);


    @Query("select dt.idAppManifiestoDetalle,m.nombreCliente as cliente,m.numeroManifiesto,m.fechaRecoleccion,c.nombre as residuo,dt.tratamiento,valor as peso, b.codeQr as codigoQr,0 as indexEtiqueta, dt.cantidadTotalEtiqueta as totalEtiqueta " +
            " from tb_manifiesto_detalle_pesos b" +
            " inner join tb_manifiestos_detalle dt on b.idAppManifiestoDetalle=dt.idAppManifiesto and dt.estadoChek=1" +
            " inner join tb_manifiestos m on dt.idAppManifiesto=m.idAppManifiesto and m.idAppManifiesto=:idAppManifiesto" +
            " inner join tb_catalogos c on dt.idTipoDesecho = c.idSistema and c.tipo=2 order by m.idAppManifiesto,dt.idAppManifiestoDetalle,b._id")
    public  abstract List<ItemEtiqueta> consultarBultosImpresion(Integer idAppManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insertValores(ManifiestoDetallePesosEntity entity);

    public long saveValores (int idManifiesto, int idManifiestoDetalle, double valor, String descricpion,Integer tipoPaquete,String codigo){
        ManifiestoDetallePesosEntity r = new ManifiestoDetallePesosEntity(valor,idManifiesto,idManifiestoDetalle,descricpion,tipoPaquete, AppDatabase.getUUID(codigo));
        return insertValores(r);
    }


}