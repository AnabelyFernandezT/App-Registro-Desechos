package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.CodigoQrTransportistaEntity;
import com.caircb.rcbtracegadere.models.ItemQrLote;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;

import java.util.List;

@Dao
public abstract class CodigoQrTransportistaDao {

    @Query("select * from tb_codigoqrtransportista")
    public abstract CodigoQrTransportistaEntity fetchCodigoQr();

    @Query("select * from tb_codigoqrtransportista where idLote=:idLote")
    public abstract CodigoQrTransportistaEntity fetchCodigoQrLote(String idLote);

    @Query("select idCodigoQr,codigoQr from tb_codigoqrtransportista")
    @Transaction
    public abstract CodigoQrTransportistaEntity fetchCodigoQr2();

    @Query("select idCodigoQr,codigoQr, fechaCierre as fecha  from tb_codigoqrtransportista")
    public abstract List<ItemQrLote> fetchListaLote();

    @Query("delete from tb_codigoqrtransportista")
    public abstract void deleteTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createCodigoQr(CodigoQrTransportistaEntity entity);

    public void saveOrUpdate(DtoCodigoQrTransportista codigoQrTransportistas) {

        CodigoQrTransportistaEntity codigoQrTransportistasEntity = fetchCodigoQrLote(codigoQrTransportistas.getLoteProcesoId());
        if (codigoQrTransportistasEntity == null) {
            codigoQrTransportistasEntity = new CodigoQrTransportistaEntity();
            codigoQrTransportistasEntity.setCodigoQr(codigoQrTransportistas.getCodigoQr());
            codigoQrTransportistasEntity.setFechaCierre(codigoQrTransportistas.getFechaCierreLote());
            codigoQrTransportistasEntity.setIdLote(codigoQrTransportistas.getLoteProcesoId());
        } else {
            codigoQrTransportistasEntity.setCodigoQr(codigoQrTransportistas.getCodigoQr());
            codigoQrTransportistasEntity.setFechaCierre(codigoQrTransportistas.getFechaCierreLote());
            codigoQrTransportistasEntity.setIdLote(codigoQrTransportistas.getLoteProcesoId());

        }
        if (codigoQrTransportistasEntity!=null) createCodigoQr(codigoQrTransportistasEntity);
    }

}
