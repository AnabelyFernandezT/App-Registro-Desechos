package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.CodigoQrTransportistaEntity;
import com.caircb.rcbtracegadere.database.entity.ConsultarFirmaUsuarioEntity;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.response.DtoFirmaUsuario;

@Dao
public abstract class ConsultarFirmaUsuarioDao {

    @Query("select * from tb_firma_usuario")
    public abstract ConsultarFirmaUsuarioEntity fetchFirmaUsuario();

    @Query("select idFirma,firmaBase64 from tb_firma_usuario")
    @Transaction
    public abstract ConsultarFirmaUsuarioEntity fetchFirmaUsuario2();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createFirmaUsuario(ConsultarFirmaUsuarioEntity entity);

    public void saveOrUpdate(DtoFirmaUsuario dtoFirmaUsuario) {

        ConsultarFirmaUsuarioEntity consultarFirmaUsuarioEntity = fetchFirmaUsuario2();
        if (consultarFirmaUsuarioEntity == null) {
            consultarFirmaUsuarioEntity = new ConsultarFirmaUsuarioEntity();
            consultarFirmaUsuarioEntity.setIdFirma(0);
            consultarFirmaUsuarioEntity.setFirmaBase64(dtoFirmaUsuario.getFirmaBase64());

        } else {
            consultarFirmaUsuarioEntity.setIdFirma(0);
            consultarFirmaUsuarioEntity.setFirmaBase64(dtoFirmaUsuario.getFirmaBase64());

        }
        if (consultarFirmaUsuarioEntity!=null) createFirmaUsuario(consultarFirmaUsuarioEntity);
    }
}
