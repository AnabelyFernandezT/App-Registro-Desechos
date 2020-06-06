package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;

@Dao
public abstract class TecnicoDao {


    @Query("select * from tb_tecnicos where identificacion=:identificacion")
    public abstract TecnicoEntity fechConsultaTecnicobyIdentidad(String identificacion);

    @Query("select * from tb_tecnicos where _id=:idTecnico")
    public abstract TecnicoEntity fechConsultaTecnicobyIdTecnico(Integer idTecnico);

    @Query("select * from tb_tecnicos where idManifiesto=:idManifiesto")
    public abstract TecnicoEntity fechConsultaTecnicobyManifiesto(Integer idManifiesto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long createTecnico(TecnicoEntity entity);

    @Query("delete from tb_tecnicos")
    public abstract void deleteTecnico();

    public long saveOrUpdate(Integer idManifiesto, String cedula, String nombre, String correo, String telefono ){
        TecnicoEntity tecnico = fechConsultaTecnicobyIdentidad(cedula);
        if(tecnico==null) {
            tecnico = new TecnicoEntity(idManifiesto,nombre,cedula,correo,telefono);
        }else{
            tecnico.setIdManifiesto(idManifiesto);
            tecnico.setNombre(nombre);
            tecnico.setCorreo(correo);
            tecnico.setTelefono(telefono);

        }

        return  createTecnico(tecnico);
    }


}
