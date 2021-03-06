package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.ParametroEntity;


@Dao
public abstract class ParametroDao {

    @Query("select valor from tb_parametros where nombre =:nombre")
    public abstract String fecthParametroValor(String nombre);

    @Query("select valor from tb_parametros where nombre=:parametro")
    public abstract String fecthParametroValorByNombre(String parametro);

    @Query("select * from tb_parametros where nombre=:parametro")
    public abstract ParametroEntity fetchParametroEspecifico(String parametro);

    /*@Query("select * from tb_parametros where nombre LIKE '% parametro %' ")
    public abstract ParametroEntity fetchParametroEspecificoLike(String parametro);*/

    @Query("delete from tb_parametros where nombre=:parametro")
    public abstract void eliminarLotes(String parametro);

    @Query("delete from tb_parametros where nombre=:nombreCheck")
    public abstract void eliminarChecksTara(String nombreCheck);


    @Query("delete from tb_parametros")
    public abstract void eliminar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createParametro(ParametroEntity entity);

    public void saveOrUpdate(String nombre, String valor){
        ParametroEntity parametro = fetchParametroEspecifico(nombre);
        if(parametro==null) {
            parametro = new ParametroEntity(nombre,valor);
        }else{
            parametro.setValor(valor);
        }

        createParametro(parametro);
    }
}
