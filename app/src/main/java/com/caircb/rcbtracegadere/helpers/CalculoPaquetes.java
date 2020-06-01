package com.caircb.rcbtracegadere.helpers;

public class CalculoPaquetes {



public void calculoPaquetes (Integer n, Integer m , Integer adicionales, Integer guardian, Integer fundas){
    int pqh, adGuardian,adFunda ;
    if (adicionales== 1 && guardian == 1 && fundas ==1 ){
        pqh = calculoPQH(n,m,adicionales,guardian,fundas);
        adGuardian = calculoGuardia(n,m,adicionales,guardian,fundas,pqh);
        adFunda = calculoFunta(n,m,adicionales,guardian,fundas,pqh);
    }else{
        if(adicionales==1 && guardian==1){
            pqh = calculoPQH(n,m,adicionales,guardian,fundas);
            adGuardian = calculoGuardia(n,m,adicionales,guardian,fundas,pqh);
        }else{
            if(adicionales == 1 && fundas ==1){
                pqh = calculoPQH(n,m,adicionales,guardian,fundas);
                adFunda = calculoFunta(n,m,adicionales,guardian,fundas,pqh);
            }

        }

    }

}




    private Integer calculoPQH(Integer n, Integer m , Integer adicionales, Integer guardian, Integer fundas){
        int pqh=0;
        if (adicionales== 1 && guardian == 1 && fundas ==1 ){
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

        }else if (guardian == 1){
                pqh = n;
        }else if (fundas==1){
            pqh =m;
        }else {
            pqh = (m+n)/2;
        }
        return pqh;
    }

    private Integer calculoGuardia(Integer n, Integer m, Integer adicionales, Integer guardian, Integer fundas, Integer pqh ){
        int adGuardian=0;
        if (adicionales== 1 && guardian == 1 && fundas ==1){
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
            if (adicionales==1 && guardian==1){
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

    private Integer calculoFunta(Integer n, Integer m, Integer adicionales, Integer guardian, Integer fundas, Integer pqh){
        int adFunda=0;
        if (adicionales== 1 && guardian == 1 && fundas ==1){
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
            if (adicionales == 1 && fundas == 1){
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
