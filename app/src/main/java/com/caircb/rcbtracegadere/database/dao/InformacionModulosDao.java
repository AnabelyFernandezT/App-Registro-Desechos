package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.models.ItemInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;


import java.util.List;

@Dao
public abstract class InformacionModulosDao {

    @Query("select * from tb_informacionModulos")
    public abstract InformacionModulosEntity fetchInformacionModulos();

    @Query("select * from tb_informacionModulos")
    @Transaction
    public abstract List<ItemInformacionModulos> fetchLote2();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createInformacionModulos(InformacionModulosEntity entity);

    public void saveOrUpdate(List<DtoInformacionModulos> informacionModulos) {
        for (DtoInformacionModulos c : informacionModulos) {
            InformacionModulosEntity informacionModulosEntity = fetchInformacionModulos();
            if (informacionModulosEntity == null) {
                informacionModulosEntity = new InformacionModulosEntity();
            } else {
                informacionModulosEntity.setRuta(c.getRuta());
                informacionModulosEntity.setSubruta(c.getSubruta());
                informacionModulosEntity.setPlaca(c.getPlaca());
                informacionModulosEntity.setChofer(c.getChofer());
                informacionModulosEntity.setAuxiliarRecoleccion1(c.getAuxiliarRecoleccion1());
                informacionModulosEntity.setAuxiliarRecoleccion2(c.getAuxiliarRecoleccion2());
                informacionModulosEntity.setKilometrajeInicio(c.getKilometrajeInicio());
                informacionModulosEntity.setResiduoSujetoFiscalizacion(c.getResiduoSujetoFiscalizacion());
                informacionModulosEntity.setRequiereDevolucioneRecipientes(c.getRequiereDevolucioneRecipientes());
                informacionModulosEntity.setTieneDisponibilidadMontacarga(c.getTieneDisponibilidadMontacarga());
                informacionModulosEntity.setTieneDisponibilidadBalanza(c.getTieneDisponibilidadBalanza());
                informacionModulosEntity.setRequiereIncineracionPresenciada(c.getRequiereIncineracionPresenciada());
                informacionModulosEntity.setObservacionResuduos(c.getObservacionResuduos());

                createInformacionModulos(informacionModulosEntity);
            }
        }
    }
}
