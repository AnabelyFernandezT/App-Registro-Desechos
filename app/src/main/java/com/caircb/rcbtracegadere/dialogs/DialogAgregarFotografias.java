package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.utils.ImagenUtils;
import com.caircb.rcbtracegadere.utils.Utils;

import java.io.File;
import java.util.List;

public class DialogAgregarFotografias extends MyDialog implements  View.OnClickListener {

    LinearLayout btnPhotosBack, btnPhoto1, btnPhoto2, btnPhoto3, btnPhoto4;
    ImagenUtils imagenUtils;
    Intent intent;
    Uri mImageCaptureUri;
    ImageView imgPhoto1, imgPhoto2, imgPhoto3, imgPhoto4;

    Integer manifiestoID,catalogoID,tipo,status;
    Integer prefix=100;

    public interface OnAgregarFotosListener {
        void onSuccessful(Integer cantidad);
    }

    private OnAgregarFotosListener mOnAgregarFotosListener;



    public DialogAgregarFotografias(@NonNull Context context,
                                    @NonNull Integer manifiestoID,
                                    @NonNull Integer catalogoID,
                                    @NonNull Integer tipo,
                                    @NonNull Integer status) {
        super(context, R.layout.dialog_agregar_fotos);
        this.context = context;
        this.manifiestoID=manifiestoID;
        this.catalogoID=catalogoID;
        this.tipo=tipo;
        this.status=status;
        this.prefix = 100 * tipo;
        //this.position=position;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        initPhotoError();
        init();
        loadData();
    }

    private void init() {
        imagenUtils = new ImagenUtils(getActivity());
        btnPhotosBack = getView().findViewById(R.id.btnPhotosBack);
        btnPhotosBack.setOnClickListener(this);

        btnPhoto1 = getView().findViewById(R.id.btnPhoto1);
        imgPhoto1 = getView().findViewById(R.id.imgPhoto1);
        btnPhoto2 = getView().findViewById(R.id.btnPhoto2);
        imgPhoto2 = getView().findViewById(R.id.imgPhoto2);
        btnPhoto3 = getView().findViewById(R.id.btnPhoto3);
        imgPhoto3 = getView().findViewById(R.id.imgPhoto3);
        btnPhoto4 = getView().findViewById(R.id.btnPhoto4);
        imgPhoto4 = getView().findViewById(R.id.imgPhoto4);

        btnPhoto1.setOnClickListener(this);
        btnPhoto2.setOnClickListener(this);
        btnPhoto3.setOnClickListener(this);
        btnPhoto4.setOnClickListener(this);

    }

    private Integer getPrefix(int index){
        return (this.prefix + index);
    }

    private void loadData(){
        List<ItemFoto> fotos =  MyApp.getDBO().manifiestoFileDao().obtenerFotografiabyManifiestoCatalogo(manifiestoID,catalogoID,tipo,status);
        if(fotos.size()>0){
            for (ItemFoto f :fotos){
               if( getPrefix(1).toString().equals(f.getCode().toString())) {
                   setImagenToImagenView(null, imgPhoto1, f.getFoto());
               }else if (getPrefix(2).toString().equals(f.getCode().toString())) {
                   setImagenToImagenView(null, imgPhoto2, f.getFoto());
               }else if (getPrefix(3).toString().equals(f.getCode().toString())) {
                   setImagenToImagenView(null, imgPhoto3, f.getFoto());
               }else if(getPrefix(4).toString().equals(f.getCode().toString())) {
                   setImagenToImagenView(null, imgPhoto4, f.getFoto());
               }
            }
        }
    }


    public void setMakePhoto(Integer code) {

        if (mImageCaptureUri != null) {
            String str = imagenUtils.compressImage(mImageCaptureUri);
            if (str != null && str.length() > 0) {
                if( getPrefix(1).toString().equals(code.toString())) {
                    setImagenToImagenView(code, imgPhoto1, str);
                }else if (getPrefix(2).toString().equals(code.toString())) {
                    setImagenToImagenView(code, imgPhoto2, str);
                }else if (getPrefix(3).toString().equals(code.toString())) {
                    setImagenToImagenView(code, imgPhoto3, str);
                }else if(getPrefix(4).toString().equals(code.toString())) {
                    setImagenToImagenView(code, imgPhoto4, str);
                }
            }
        }
    }



    private void setImagenToImagenView(Integer code, ImageView img, String str) {
        img.setImageBitmap(Bitmap.createScaledBitmap(Utils.StringToBitMap(str), 100, 100, true));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(code!=null) {
            MyApp.getDBO().manifiestoFileDao().saveOrUpdate(manifiestoID, catalogoID, code, tipo, str,status);
        }
    }

    private void openCamera(Integer index) {
        try {
            File dir=null;
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                dir = new File(getActivity().getFilesDir(), "evidencias");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, "fotografia.jpg");
                Uri photoUri = FileProvider.getUriForFile(getContext(), MyApp.MY_PROVIDER, file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                mImageCaptureUri = Uri.fromFile(file);
            }else{

                dir = new File(android.os.Environment.getExternalStorageDirectory(), "fotografia.jpg");
                mImageCaptureUri = Uri.fromFile(dir);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            }

            getActivity().startActivityForResult(intent, index);

        }catch (Exception ex){
            messageBox(ex.getMessage());
        }
    }


    private void initPhotoError(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public void setOnAgregarFotosListener(@NonNull OnAgregarFotosListener l){
        mOnAgregarFotosListener =l;
    }

    private void selectImage(final Integer index) {
        final CharSequence[] options = {"Abrir Camara", "Abrir Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agregar Fotografia");
        builder.setItems(options, new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Abrir Camara"))
                {
                   openCamera(getPrefix(index));
                }
                else if (options[item].equals("Abrir Galeria"))
                {
                    intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(Intent.createChooser(intent,""),index);
                }
                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPhotosBack:
                if (mOnAgregarFotosListener != null) {
                    mOnAgregarFotosListener.onSuccessful(MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(manifiestoID,catalogoID,tipo));
                }
                dismiss();
                break;
            case R.id.btnPhoto1:
                openCamera(getPrefix(1));
                break;
            case R.id.btnPhoto2:
                openCamera(getPrefix(2));
                break;
            case R.id.btnPhoto3:
                openCamera(getPrefix(3));
                break;
            case R.id.btnPhoto4:
                openCamera(getPrefix(4));
                break;
        }
    }


}
