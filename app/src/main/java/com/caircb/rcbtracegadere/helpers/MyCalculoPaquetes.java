package com.caircb.rcbtracegadere.helpers;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.models.CalculoPaqueteResul;

public class MyCalculoPaquetes {

    ManifiestoPaquetesEntity mpkg;
    Integer idAppManifiesto;
    Integer tipoPaquete;

    public  MyCalculoPaquetes(Integer idAppManifiesto,Integer tipoPaquete){
        this.idAppManifiesto=idAppManifiesto;
        this.tipoPaquete=tipoPaquete;
    }

    public CalculoPaqueteResul algoritmo (PaqueteEntity pkg){
        CalculoPaqueteResul resp=null;
        if(pkg!=null) {

            mpkg = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(idAppManifiesto, tipoPaquete);
            Integer n = mpkg.getDatosFundas();
            Integer m = mpkg.getDatosGuardianes();
            boolean adicionales = pkg.getFlagAdicionales(), guardian = pkg.getFlagAdicionalGuardian(), fundas = pkg.getFlagAdicionalFunda();

            resp = new CalculoPaqueteResul();

            if (adicionales == true && guardian == true && fundas == true) {
                resp.setPqh(calculoPQH(n, m, adicionales, guardian, fundas));
                resp.setAdicionalGuardian(calculoGuardia(n, m, adicionales, guardian, fundas, resp.getPqh()));
                resp.setAdicionalFunda(calculoFunta(n, m, adicionales, guardian, fundas, resp.getAdicionalFunda()));
            } else {
                if (adicionales == true && guardian == true) {
                    resp.setPqh(calculoPQH(n, m, adicionales, guardian, fundas));
                    resp.setAdicionalGuardian(calculoGuardia(n, m, adicionales, guardian, fundas, resp.getPqh()));
                } else {
                    if (adicionales == true && fundas == true) {
                        resp.setPqh(calculoPQH(n, m, adicionales, guardian, fundas));
                        resp.setAdicionalFunda(calculoFunta(n, m, adicionales, guardian, fundas, resp.getPqh()));
                    }
                }
            }

            //update datos en tabla de calculo de algoritmo...
            mpkg.setPqh(resp.getPqh());
            mpkg.setAdFundas(resp.getAdicionalFunda());
            mpkg.setAdGuardianes(resp.getAdicionalGuardian());
            MyApp.getDBO().manifiestoPaqueteDao().actualiarPaquete(mpkg);
        }
        return resp;
    }




    private Integer calculoPQH(Integer n, Integer m , boolean adicionales, boolean guardian, boolean fundas){
        int pqh=0;
        if (adicionales== true && guardian == true && fundas ==true ){
            if (n==0 || m== 0){
                if (n>=0){
                    pqh = 1;
                } else if(m==1){
                    pqh = 1;
                }else if (n==1){
                    if(m==0){
                        pqh=1;
                    }else if(n==0){
                        pqh=1;
                    }else if (m==1){
                        pqh = 1;
                    }else {
                        //pqh = false;
                    }

                }else {
                    //pqh = false;
                }

            }else if(n>=1 || m>=1){
                pqh = (m < n) ? m : n;
            }else{
                //pqh = false;
            }

        }else if (guardian == true){
                pqh = n;
        }else if (fundas==true){
            pqh =m;
        }else {
            pqh = (m+n)/2;
        }
        return pqh;
    }

    private Integer calculoGuardia(Integer n, Integer m, boolean adicionales, boolean guardian, boolean fundas, Integer pqh ){
        int adGuardian=0;
        if (adicionales== true && guardian == true && fundas ==true){
            if(m==0 || n ==0 ){
                if (m>0){
                    adGuardian = m-pqh;
                }else {
                    adGuardian = 0;
                }
            }else {
                if(m>n){
                    adGuardian = m-n;
                }else {
                    adGuardian = 0;
                }
            }
        }else {
            if (adicionales==true && guardian==true){
                if (n>m){
                    adGuardian=m-n;
                }else{
                    adGuardian=0;
                }
            }else {
                adGuardian = 0;
            }
        }
        return adGuardian;
    }

    private Integer calculoFunta(Integer n, Integer m, boolean adicionales, boolean guardian, boolean fundas, Integer pqh){
        int adFunda=0;
        if (adicionales== true && guardian == true && fundas ==true){
            if (m==0||n==0){
                if (n>0){
                    adFunda=n-pqh;
                }else {
                    adFunda=0;
                }
            }else {
                if (n>m){
                    adFunda = n-m;
                }else {
                    adFunda=0;
                }
            }

        }else{
            if (adicionales == true && fundas == true){
                if (n>m){
                    adFunda = n-m;
                }else {
                    adFunda = 0;
                }

            }else {
                adFunda=0;
            }
        }
        return adFunda;

    }
}
