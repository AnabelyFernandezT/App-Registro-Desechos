package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.models.ItemInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionModulos;

import java.util.List;

@Dao
public abstract class InformacionModulosDao {

    @Query("select * from tb_informacionModulos")
    public abstract InformacionModulosEntity fetchInformacionModulos();

    @Query("select ruta, subruta, placa, chofer, auxiliarRecoleccion1,auxiliarRecoleccion2,kilometrajeInicio,residuoSujetoFiscalizacion,requiereDevolucionRecipientes" +
            ",tieneDisponibilidadMontacarga,tieneDisponibilidadBalanza,requiereIncineracionPresenciada,observacionResiduos,idLoteProceso from tb_informacionModulos")
    @Transaction
    public abstract InformacionModulosEntity fetchInformacionModulos2();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createInformacionModulos(InformacionModulosEntity entity);

    public void saveOrUpdate(List<DtoInformacionModulos> informacionModulos) {

            InformacionModulosEntity informacionModulosEntity = fetchInformacionModulos();
            if (informacionModulosEntity == null) {
                informacionModulosEntity = new InformacionModulosEntity();
                informacionModulosEntity.setIdInformacionModulos(0);
                informacionModulosEntity.setRuta(informacionModulos.get(0).getRuta());
                informacionModulosEntity.setSubruta(informacionModulos.get(0).getSubruta());
                informacionModulosEntity.setPlaca(informacionModulos.get(0).getPlaca());
                informacionModulosEntity.setChofer(informacionModulos.get(0).getChofer());
                informacionModulosEntity.setAuxiliarRecoleccion1(informacionModulos.get(0).getAuxiliarRecoleccion1());
                informacionModulosEntity.setAuxiliarRecoleccion2(informacionModulos.get(0).getAuxiliarRecoleccion2());
                informacionModulosEntity.setKilometrajeInicio(informacionModulos.get(0).getKilometrajeInicio());
                informacionModulosEntity.setResiduoSujetoFiscalizacion(informacionModulos.get(0).getResiduoSujetoFiscalizacion());
                informacionModulosEntity.setRequiereDevolucionRecipientes(informacionModulos.get(0).getRequiereDevolucionRecipientes());
                informacionModulosEntity.setTieneDisponibilidadMontacarga(informacionModulos.get(0).getTieneDisponibilidadMontacarga());
                informacionModulosEntity.setTieneDisponibilidadBalanza(informacionModulos.get(0).getTieneDisponibilidadBalanza());
                informacionModulosEntity.setRequiereIncineracionPresenciada(informacionModulos.get(0).getRequiereIncineracionPresenciada());
                informacionModulosEntity.setObservacionResiduos(informacionModulos.get(0).getObservacionResiduos());
                informacionModulosEntity.setIdLoteProceso(informacionModulos.get(0).getIdLoteProceso());
            } else {
                informacionModulosEntity.setIdInformacionModulos(0);
                informacionModulosEntity.setRuta(informacionModulos.get(0).getRuta());
                informacionModulosEntity.setSubruta(informacionModulos.get(0).getSubruta());
                informacionModulosEntity.setPlaca(informacionModulos.get(0).getPlaca());
                informacionModulosEntity.setChofer(informacionModulos.get(0).getChofer());
                informacionModulosEntity.setAuxiliarRecoleccion1(informacionModulos.get(0).getAuxiliarRecoleccion1());
                informacionModulosEntity.setAuxiliarRecoleccion2(informacionModulos.get(0).getAuxiliarRecoleccion2());
                informacionModulosEntity.setKilometrajeInicio(informacionModulos.get(0).getKilometrajeInicio());
                informacionModulosEntity.setResiduoSujetoFiscalizacion(informacionModulos.get(0).getResiduoSujetoFiscalizacion());
                informacionModulosEntity.setRequiereDevolucionRecipientes(informacionModulos.get(0).getRequiereDevolucionRecipientes());
                informacionModulosEntity.setTieneDisponibilidadMontacarga(informacionModulos.get(0).getTieneDisponibilidadMontacarga());
                informacionModulosEntity.setTieneDisponibilidadBalanza(informacionModulos.get(0).getTieneDisponibilidadBalanza());
                informacionModulosEntity.setRequiereIncineracionPresenciada(informacionModulos.get(0).getRequiereIncineracionPresenciada());
                informacionModulosEntity.setObservacionResiduos(informacionModulos.get(0).getObservacionResiduos());
                informacionModulosEntity.setIdLoteProceso(informacionModulos.get(0).getIdLoteProceso());
            }
        if (informacionModulosEntity!=null) createInformacionModulos(informacionModulosEntity);
    }
}
