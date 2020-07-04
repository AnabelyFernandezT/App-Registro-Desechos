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


    @Query("select _id as idCatalogo, valor,descripcion as tipo, impresion from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle ")
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

    @Query ("update tb_manifiesto_detalle_pesos set impresion =:impresion where _id=:id")
    public abstract void updateBanderaImpresion( Integer id, boolean impresion);

    @Query("delete from tb_manifiesto_detalle_pesos where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle and impresion=0")
    public abstract void deleteTableValoresNoConfirmados(Integer idManifiesto, Integer idManifiestoDetalle);

    @Query("Select sum(valor) from tb_manifiesto_detalle_pesos b where idAppManifiesto=:idManifiesto and idAppManifiestoDetalle=:idManifiestoDetalle ")
    public abstract double sumaPesoFinal(Integer idManifiesto, Integer idManifiestoDetalle);

    @Query("select dt.idAppManifiestoDetalle,m.nombreCliente as cliente,m.numeroManifiesto,m.fechaRecoleccion,c.nombre as residuo,dt.tratamiento,valor as peso, b.codeQr as codigoQr,0 as indexEtiqueta, dt.cantidadTotalEtiqueta as totalEtiqueta " +
            " from tb_manifiesto_detalle_pesos b" +
            " inner join tb_manifiestos_detalle dt on b.idAppManifiestoDetalle=dt.idAppManifiestoDetalle and dt.estadoChek=1" +
            " inner join tb_manifiestos m on dt.idAppManifiesto=m.idAppManifiesto and m.idAppManifiesto=:idAppManifiesto" +
            " inner join tb_catalogos c on dt.idTipoDesecho = c.idSistema and c.tipo=2 order by m.idAppManifiesto,dt.idAppManifiestoDetalle,b._id")
    public  abstract List<ItemEtiqueta> consultarBultosImpresion(Integer idAppManifiesto);

    @Query("select dt.idAppManifiestoDetalle,m.nombreCliente as cliente,m.numeroManifiesto,rif.fechaInicio as fechaRecoleccion," +
            "c.nombre as residuo,dt.tratamiento,valor as peso, b.codeQr as codigoQr,0 as indexEtiqueta, dt.cantidadTotalEtiqueta as totalEtiqueta " +
            " from tb_manifiesto_detalle_pesos b" +
            " inner join tb_manifiestos_detalle dt on b.idAppManifiestoDetalle=dt.idAppManifiestoDetalle  " +
            " inner join tb_manifiestos m on dt.idAppManifiesto=m.idAppManifiesto " +
            " inner join tb_catalogos c on dt.idTipoDesecho = c.idSistema and c.tipo=2 " +
            "inner join tb_rutaInicioFin rif on rif.idSubRuta = m.idSubRuta " +
            " where m.idAppManifiesto=:idAppManifiesto and dt.idAppManifiestoDetalle =:idManifiestoDetalle and b._id =:idCatalogo " +
            " order by m.idAppManifiesto,dt.idAppManifiestoDetalle,b._id")
    public  abstract ItemEtiqueta consultaBultoIndividual(Integer idAppManifiesto, Integer idManifiestoDetalle, Integer idCatalogo);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insertValores(ManifiestoDetallePesosEntity entity);

    public long saveValores (int idManifiesto, int idManifiestoDetalle, double valor, String descricpion,Integer tipoPaquete,String codigo, boolean impresion){
        ManifiestoDetallePesosEntity r = new ManifiestoDetallePesosEntity(valor,idManifiesto,idManifiestoDetalle,descricpion,tipoPaquete, AppDatabase.getUUID(codigo), impresion);
        return insertValores(r);
    }


}