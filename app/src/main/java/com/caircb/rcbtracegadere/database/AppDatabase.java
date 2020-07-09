package com.caircb.rcbtracegadere.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.caircb.rcbtracegadere.database.dao.CatalogoDao;
import com.caircb.rcbtracegadere.database.dao.InformacionModulosDao;
import com.caircb.rcbtracegadere.database.dao.LogDao;
import com.caircb.rcbtracegadere.database.dao.LoteDao;
import com.caircb.rcbtracegadere.database.dao.LoteHotelesDao;
import com.caircb.rcbtracegadere.database.dao.LotePadreDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoDetalleDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoDetallePesosDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoMotivosNoRecoleccionDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoObservacionFrecuenteDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPlantaDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPlantaDetalleDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPlantaDetalleValorDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoSedeDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoSedeDetalleDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoSedeDetalleValorDao;
import com.caircb.rcbtracegadere.database.dao.PaqueteDao;
import com.caircb.rcbtracegadere.database.dao.ImpresoraDao;
import com.caircb.rcbtracegadere.database.dao.ParametroDao;
import com.caircb.rcbtracegadere.database.dao.RutaInicioFinDao;
import com.caircb.rcbtracegadere.database.dao.RutasDao;
import com.caircb.rcbtracegadere.database.dao.RuteoRecoleccionDao;
import com.caircb.rcbtracegadere.database.dao.TecnicoDao;
import com.caircb.rcbtracegadere.database.dao.LoteDao;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.database.entity.LogEntity;
import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.LoteHotelesEntity;
import com.caircb.rcbtracegadere.database.entity.LotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoFileEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoMotivoNoRecoleccionEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoObservacionFrecuenteEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaDetalleValorEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPlantaEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeDetalleValorEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoSedeEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.database.entity.ImpresoraEntity;

import com.caircb.rcbtracegadere.helpers.MyConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Database(entities = {
        ManifiestoDetallePesosEntity.class,
        ParametroEntity.class,
        CatalogoEntity.class,
        RutaInicioFinEntity.class,
        TecnicoEntity.class,
        LogEntity.class,
        ManifiestoEntity.class,
        ManifiestoDetalleEntity.class,
        ManifiestoObservacionFrecuenteEntity.class,
        ManifiestoFileEntity.class,
        ManifiestoMotivoNoRecoleccionEntity.class,
        ImpresoraEntity.class,
        PaqueteEntity.class,
        ManifiestoPaquetesEntity.class,
        RutasEntity.class,
        LoteEntity.class,
        ManifiestoSedeEntity.class,
        ManifiestoSedeDetalleValorEntity.class,
        InformacionModulosEntity.class,
        ManifiestoSedeDetalleEntity.class,
        LoteHotelesEntity.class,
        LotePadreEntity.class,
        RuteoRecoleccionEntity.class,
        ManifiestoPlantaEntity.class,
        ManifiestoPlantaDetalleEntity.class,
        ManifiestoPlantaDetalleValorEntity.class
},version = MyConstant.DBO_VERSION)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static Date getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        try {
            return dateFormat.parse(dateFormat.format( new Date()));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateFormat.format( new Date()));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        try {
            return dateFormat.format( new Date());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFieldName(String prefix){
        if(prefix!=null && prefix.length()>0){
            return prefix+"_"+ System.currentTimeMillis()+".jpg";
        }else{
            return "RCB_"+ System.currentTimeMillis()+".jpg";
        }
    }

    public static String getUUID(String prefix){
        if(prefix!=null && prefix.length()>0){
            return prefix+"$"+ System.currentTimeMillis();
        }else{
            return  ""+System.currentTimeMillis();
        }
    }

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (AppDatabase.class){
                if(sInstance==null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            MyConstant.DBO_NAME)
                            .allowMainThreadQueries()
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract ParametroDao parametroDao();
    public abstract CatalogoDao catalogoDao();
    public abstract TecnicoDao tecnicoDao();
    public abstract RutaInicioFinDao rutaInicioFinDao();
    public abstract LogDao logDao();
    public abstract ManifiestoDao manifiestoDao();
    public abstract ManifiestoDetalleDao manifiestoDetalleDao();
    public abstract ManifiestoObservacionFrecuenteDao manifiestoObservacionFrecuenteDao();
    public abstract ManifiestoFileDao manifiestoFileDao();
    public abstract PaqueteDao paqueteDao();
    public abstract ManifiestoMotivosNoRecoleccionDao manifiestoMotivosNoRecoleccionDao();
    public  abstract ManifiestoDetallePesosDao manifiestoDetallePesosDao();
    public abstract ImpresoraDao impresoraDao();
    public abstract ManifiestoPaqueteDao manifiestoPaqueteDao();
    public abstract RutasDao rutasDao();
    public abstract LoteDao loteDao();
    public abstract ManifiestoSedeDao manifiestoSedeDao();
    public abstract ManifiestoSedeDetalleDao manifiestoDetalleSede();
    public abstract ManifiestoSedeDetalleValorDao manifiestoDetalleValorSede();
    public abstract InformacionModulosDao informacionModulosDao();
    public abstract LoteHotelesDao loteHotelesDao();
    public abstract RuteoRecoleccionDao ruteoRecoleccion();
    public abstract LotePadreDao lotePadreDao();
    public abstract ManifiestoPlantaDao manifiestoPlantaDao();
    public abstract ManifiestoPlantaDetalleDao manifiestoPlantaDetalleDao();
    public abstract ManifiestoPlantaDetalleValorDao manifiestoPlantaDetalleValorDao();

}
