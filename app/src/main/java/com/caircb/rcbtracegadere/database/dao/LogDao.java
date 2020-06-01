package com.caircb.rcbtracegadere.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.caircb.rcbtracegadere.database.entity.LogEntity;

import java.util.List;

@Dao
public abstract class LogDao {


    @Query("select * from tb_logs")
    public  abstract List<LogEntity> listarLog();


}
