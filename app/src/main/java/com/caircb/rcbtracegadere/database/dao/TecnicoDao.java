package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;

@Dao
public abstract class TecnicoDao {


    @Query("select * from tb_tecnicos where identificacion=:identificacion")
    public abstract TecnicoEntity fechConsultaTecnicobyIdentidad(String identificacion);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createTecnico(TecnicoEntity entity);

    public void saveOrUpdate(Integer idManifiesto, String cedula, String nombre, String correo, String telefono ){
        TecnicoEntity tecnico = fechConsultaTecnicobyIdentidad(cedula);
        if(tecnico==null) {
            tecnico = new TecnicoEntity(idManifiesto,nombre,cedula,correo,telefono);
        }else{
            tecnico.setIdManifiesto(idManifiesto);
            tecnico.setNombre(nombre);
            tecnico.setCorreo(correo);
            tecnico.setTelefono(telefono);

        }

        createTecnico(tecnico);
    }


}
