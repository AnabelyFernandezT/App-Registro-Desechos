package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.CodigoQrTransportistaEntity;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;

import java.util.List;

@Dao
public abstract class CodigoQrTransportistaDao {

    @Query("select * from tb_codigoqrtransportista")
    public abstract CodigoQrTransportistaEntity fetchCodigoQr();

    @Query("select idCodigoQr,codigoQr from tb_codigoqrtransportista")
    @Transaction
    public abstract CodigoQrTransportistaEntity fetchCodigoQr2();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createCodigoQr(CodigoQrTransportistaEntity entity);

    public void saveOrUpdate(DtoCodigoQrTransportista codigoQrTransportistas) {

        CodigoQrTransportistaEntity codigoQrTransportistasEntity = fetchCodigoQr();
        if (codigoQrTransportistasEntity == null) {
            codigoQrTransportistasEntity = new CodigoQrTransportistaEntity();
            codigoQrTransportistasEntity.setIdCodigoQr(0);
            codigoQrTransportistasEntity.setCodigoQr(codigoQrTransportistas.getCogigoQr());

        } else {
            codigoQrTransportistasEntity.setIdCodigoQr(0);
            codigoQrTransportistasEntity.setCodigoQr(codigoQrTransportistas.getCogigoQr());

        }
        if (codigoQrTransportistasEntity!=null) createCodigoQr(codigoQrTransportistasEntity);
    }

}
