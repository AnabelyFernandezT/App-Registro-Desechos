package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreHotel;

import java.util.List;

@Dao
public abstract class HotelLotePadreDao {

    @Query("select * from tb_hotel_padre where idTransportistaRecolector=:id")
    public abstract HotelLotePadreEntity fetchConsultarHotelLote(Integer id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void crearLoteHotel(HotelLotePadreEntity entity);

    public void saveOrUpdare(DtoLotePadreHotel loteHotel, Integer id){
        HotelLotePadreEntity hp = fetchConsultarHotelLote(id);

        if(hp==null){
            hp = new HotelLotePadreEntity();
            hp.setIdTransportistaRecolector(MySession.getIdUsuario());
            hp.setIdLoteContenedorHotel(loteHotel.getIdLoteContenedorHotel());
            hp.setCodigo(loteHotel.getCodigo());
            hp.setEstado(loteHotel.getEstado());

        }else {
            hp.setIdLoteContenedorHotel(loteHotel.getIdLoteContenedorHotel());
            hp.setCodigo(loteHotel.getCodigo());
            hp.setEstado(loteHotel.getEstado());
        }

        crearLoteHotel(hp);

    }

}
