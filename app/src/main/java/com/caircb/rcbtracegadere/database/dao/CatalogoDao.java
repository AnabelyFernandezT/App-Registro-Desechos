package com.caircb.rcbtracegadere.database.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;

import java.util.List;

@Dao
public abstract class CatalogoDao {



    @Query("select nombre ,codigo from tb_catalogos where tipo=:tipo AND idSistema in (1,4)")
    public  abstract List<DtoCatalogo> fetchConsultarCatalogo(Integer tipo);

    @Query("select idSistema as id,  nombre,codigo from tb_catalogos where tipo=:tipo")
    public  abstract List<DtoCatalogo> fetchConsultarCatalogobyTipo(Integer tipo);

    @Query("select nombre ,codigo from tb_catalogos where tipo=:tipo and idSistema =:id")
    public  abstract List<DtoCatalogo> fetchConsultarCatalogobyTipoId(Integer id, Integer tipo);


    @Query("select * from tb_catalogos where codigo=:codigo and tipo=:tipo")
    public  abstract CatalogoEntity fetchConsultarCatalogoId(String codigo, Integer tipo);

    @Query("select * from tb_catalogos where nombre=:codigo and tipo=:tipo")
    public  abstract CatalogoEntity fetchConsultarCatalogo(String codigo, Integer tipo);

    @Query("select * from tb_catalogos where tipo=:tipo and idSistema=:idSistema")
    public abstract CatalogoEntity fetchConsultarCatalogoEspecifico(Integer idSistema, Integer tipo);

    @Query("select count(*) from tb_catalogos where tipo=:tipoCatalogo LIMIT 1")
    public abstract boolean existeCatalogosEspecifico(Integer tipoCatalogo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void createCatalogo(CatalogoEntity entity);

    public void saveOrUpdate(List<DtoCatalogo> catalogos, Integer tipoCatalogo){
        for(DtoCatalogo c:catalogos) {
            CatalogoEntity catalogo = fetchConsultarCatalogoEspecifico(c.getId(), tipoCatalogo);
            if (catalogo == null) {
                catalogo = new CatalogoEntity(c.getId(), c.getCodigo(), c.getNombre(), tipoCatalogo);
            } else {
                catalogo.setCodigo(c.getCodigo());
                catalogo.setNombre(c.getNombre());
            }

            createCatalogo(catalogo);
        }
    }
}
