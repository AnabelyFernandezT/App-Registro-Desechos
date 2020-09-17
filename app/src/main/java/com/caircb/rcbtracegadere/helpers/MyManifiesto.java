package com.caircb.rcbtracegadere.helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.models.RowItemPaquete;
import com.caircb.rcbtracegadere.utils.Utils;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.PhantomReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;

import static android.graphics.Color.*;

public class MyManifiesto {

    private File pdfFile;
    Context context;
    Integer idManifiesto,idAppTipoPaquete;
    ManifiestoEntity manifiesto;
    String fundas50 = "0",fundas63="0", paquetes1="0", paquete2="0", pg = "0" , pf = "0", paquete3="0" ;
    TecnicoEntity tecnicoEntity;
    Bitmap logoMAE;
    Bitmap imagenCheck;
    Bitmap imagenUnCheck;
    Bitmap firma;
    Bitmap firmaNoRecoleccion;
    Bitmap firmaTransportista, firmaOperador1, firmaOperador2, firmaOperador1NoRecoleccion, firmaOperador2NoRecoleccion;
    ItemFile fileFirmaTecnico,fileFirmaTecnicoNoRecoleccion, fileFirmaOperador1, fileFirmaOperador2,fileFirmaOperador1NoRecoleccion,fileFirmaOperador2NoRecoleccion;
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd//MM/yyyy");
    CatalogoEntity catalogo;
    String identificacion;
    List<RowItemPaquete> listaPaquetes;
    ManifiestoPaquetesEntity manifiestoPkg;
    PaqueteEntity pkg;
    //OP-RE-01

    private String getPath() { return simpleDate.format(new Date());}
    String fecha = getPath();


    public MyManifiesto(@Nullable Context context, @Nullable Integer idManifiesto, Integer idAppTipoPaquete, String identificacion){
        this.context = context;
        this.idManifiesto = idManifiesto;
        this.idAppTipoPaquete = idAppTipoPaquete;
        this.identificacion = identificacion;

    }

    public void create(){

        if(idManifiesto!=null) {

            logoMAE = BitmapFactory.decodeResource( context.getResources(), R.mipmap.logo);
            imagenCheck = BitmapFactory.decodeResource( context.getResources(), R.mipmap.ic_check);
            imagenUnCheck = BitmapFactory.decodeResource( context.getResources(), R.mipmap.ic_uncheck);

            tecnicoEntity = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(identificacion);
            manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);
            catalogo = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(manifiesto.getNumeroPlacaVehiculo(),4);
            //RECOLECCION
            fileFirmaTecnico = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR,MyConstant.STATUS_RECOLECCION);
            if(fileFirmaTecnico!=null) firma = Utils.StringToBitMap(fileFirmaTecnico.getFile());

            fileFirmaOperador1 = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1,MyConstant.STATUS_RECOLECCION);
            if(fileFirmaOperador1!=null) firmaOperador1 = Utils.StringToBitMap(fileFirmaOperador1.getFile());

            fileFirmaOperador2 = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2,MyConstant.STATUS_RECOLECCION);
            if(fileFirmaOperador2!=null) firmaOperador2 = Utils.StringToBitMap(fileFirmaOperador2.getFile());

            //NO RECOLECCION
            fileFirmaTecnicoNoRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA,MyConstant.STATUS_NO_RECOLECCION);
            if(fileFirmaTecnicoNoRecoleccion!=null) firmaNoRecoleccion = Utils.StringToBitMap(fileFirmaTecnicoNoRecoleccion.getFile());

            fileFirmaOperador1NoRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1,MyConstant.STATUS_NO_RECOLECCION);
            if(fileFirmaOperador1NoRecoleccion!=null) firmaOperador1NoRecoleccion = Utils.StringToBitMap(fileFirmaOperador1NoRecoleccion.getFile());

            fileFirmaOperador2NoRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2,MyConstant.STATUS_NO_RECOLECCION);
            if(fileFirmaOperador2NoRecoleccion!=null) firmaOperador2NoRecoleccion = Utils.StringToBitMap(fileFirmaOperador2NoRecoleccion.getFile());



            ItemFile fileFirmaTransporte = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA,MyConstant.STATUS_RECOLECCION);
            if(fileFirmaTransporte!=null) firmaTransportista = Utils.StringToBitMap(fileFirmaTransporte.getFile());


            loadDataPaquetes();

            if(manifiesto!=null) createReport();
        }
    }

    private String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    private void createReport(){
        Document doc = new Document(PageSize.A4,12.0f,12.0f,20.0f,20.0f);

        try {

            PdfWriter.getInstance(doc, createLocalFile("reporteGadere.pdf"));

            doc.open();

            Font f12 = new Font(Font.FontFamily.HELVETICA,12);
            Font f10 = new Font(Font.FontFamily.HELVETICA,10);
            Font f6 = new Font(Font.FontFamily.HELVETICA,6);
            Font f3 = new Font(Font.FontFamily.HELVETICA,3);


            //CABECERA
            PdfPTable header = new PdfPTable(new float[] { 12, 76 ,12});
            header.setWidthPercentage(100);
            //logo...
            //InputStream ims = context.getAssets().open("logo.jpg");
            //Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            logoMAE.compress(Bitmap.CompressFormat.PNG, 100, stream);

            Image jpg = Image.getInstance(stream.toByteArray());
            jpg.scaleToFit(119,48);
            PdfPCell cell =  new PdfPCell(jpg);
            cell.setBorder(Rectangle.NO_BORDER);
            header.addCell(cell);

            Paragraph para2 = new Paragraph();
            para2.add(new Chunk("MINISTERIO DEL AMBIENTE",f12));
            para2.add(Chunk.NEWLINE);
            para2.add(new Chunk("SUBSECRETARIA DE CALIDAD AMBIENTAL",f10));
            para2.add(Chunk.NEWLINE);
            para2.add(new Chunk("SUBSECRETARIA DE GESTIÓN AMBIENTAL COSTERA",f10));
            para2.add(Chunk.NEWLINE);
            para2.add(new Chunk("MANIFIESTO ÚNICO DE ENTREGA, TRANSPORTE Y RECEPCIÓN",f10));
            para2.add(Chunk.NEWLINE);
            para2.add(new Chunk("DE DESECHOS",f10));
            para2.setLeading(15, 3);

            cell =  new PdfPCell(para2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            header.addCell(cell);

            cell =  new PdfPCell(new Phrase("CLAVE DEL MANIFIESTO"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"           OP-RE-01",f3));
            cell.setBorder(Rectangle.NO_BORDER);

            header.addCell(cell);

            header.completeRow();
            doc.add(header);
            stream.close();
            //ims.close();


            PdfPTable body = new PdfPTable(new float[] { 3, 97});
            body.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("GENERADOR",f12));
            cell.setRotation(90);
            body.addCell(cell);

            cell = new PdfPCell(createGenerador(f6));
            body.addCell(cell);
            body.completeRow();

            cell = new PdfPCell(new Phrase("TRANSPORTE",f12));
            cell.setRotation(90);
            body.addCell(cell);

            cell = new PdfPCell(createGeneradorTran(f6));
            body.addCell(cell);
            body.completeRow();

            cell = new PdfPCell(new Phrase("DESTINATARIO",f12));
            cell.setRotation(90);
            body.addCell(cell);

            cell = new PdfPCell(createGeneradorDestinatario(f6));
            body.addCell(cell);
            body.completeRow();

            cell = new PdfPCell(new Phrase("INFORMACION ADICIONAL",f12));
            cell.setRotation(90);
            body.addCell(cell);

            cell = new PdfPCell(createGeneradorInfoAdi(f6));
            body.addCell(cell);
            body.completeRow();


            //body.completeRow();

            doc.add(body);


        }catch (Exception ex){
            int x=0;
        }finally {
            doc.close();
        }
    }

    public String getPathFile(){
        return pdfFile.getAbsolutePath();
    }

    private PdfPTable createGenerador(Font f6) throws Exception {

        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);

        //tabla 1
        PdfPTable tb1 = new PdfPTable(new float[] { 30, 30,30,10});
        tb1.addCell(new PdfPCell(new Phrase("1. NÚM. DE REGISTRO COMO GENERADOR DE DESECHOS.",f6)));
        tb1.addCell(new PdfPCell(new Phrase("2. NÚM.. DE LICENCIA AMBIENTAL.",f6)));
        tb1.addCell(new PdfPCell(new Phrase("3. No. DE MANIFIESTO",f6)));
        tb1.addCell(new PdfPCell(new Phrase("4. PÁGINA",f6)));
        if(manifiesto.getNumeroGeneradorDesecho()!=null||manifiesto.getNumeroGeneradorDesecho().isEmpty()){
            tb1.addCell(createCell(manifiesto.getNumeroGeneradorDesecho(),f6));
        }else {
            tb1.addCell(createCell("",f6));
        }

        tb1.addCell(createCell("",f6));
        tb1.addCell(createCell(manifiesto.getSerie()+"  "+manifiesto.getNumeroManifiesto(),f6));
        tb1.addCell(createCell("1/2",f6));
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 2
        PdfPTable tb2 = new PdfPTable(new float[] { 27, 73});
        tb2.addCell(new PdfPCell(new Phrase("5. NOMBRE DE LA EMPRESA GENERADORA:",f6)));
        tb2.addCell(new PdfPCell(new Phrase(manifiesto.getNombreCliente(),f6)));
        tb2.addCell(new PdfPCell(new Phrase("6. REGISTRO ÚNICO DE CONTRIBUYENTES:",f6)));
        tb2.addCell(new PdfPCell(new Phrase(manifiesto.getIdentificacionCliente(),f6)));
        tb2.addCell(new PdfPCell(new Phrase("7. NOMBRE DE LA INSTALACIÓN GENERADORA:",f6)));
        tb2.addCell(new PdfPCell(new Phrase(manifiesto.getNombreCliente(),f6))); // poner sucursal
        tb2.completeRow();

        _cell = new PdfPCell(tb2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 3
        PdfPTable tb3 = new PdfPTable(new float[] { 15, 40,15,30});
        tb3.addCell(new PdfPCell(new Phrase("DOMICILIO (CALLE Y NO):",f6)));
        tb3.addCell(new PdfPCell(new Phrase(manifiesto.getDireccionCliente(),f6)));
        tb3.addCell(new PdfPCell(new Phrase("PROVINCIA:",f6)));
        tb3.addCell(new PdfPCell(new Phrase(manifiesto.getProvincia(),f6)));
        tb3.completeRow();

        _cell = new PdfPCell(tb3);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 4
        PdfPTable tb4 = new PdfPTable(new float[] { 20,30,20,30});
        tb4.addCell(new PdfPCell(new Phrase("CANTÓN:",f6)));
        tb4.addCell(new PdfPCell(new Phrase(manifiesto.getCanton(),f6)));
        tb4.addCell(new PdfPCell(new Phrase("PARROQUIA:",f6)));
        tb4.addCell(new PdfPCell(new Phrase(manifiesto.getParroquia(),f6)));

        tb4.completeRow();

        _cell = new PdfPCell(tb4);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 4.1 (11)
        PdfPTable tb11 = new PdfPTable(new float[]{40,15,45});
        tb11.addCell(new PdfPCell(new Phrase("No. ONU:",f6)));
        tb11.addCell(new PdfPCell(new Phrase("TEL: ",f6)));
        tb11.addCell(new PdfPCell(new Phrase("",f6)));
        tb11.completeRow();

        _cell =  new PdfPCell(tb11);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 5
        PdfPTable tb5 = new PdfPTable(new float[] { 45, 10, 20, 15,10});
        tb5.addCell(new PdfPCell(new Phrase("8. DESCRIPCION (Nombre del desecho de acuerdo al listado Nacional e indicar CRTIB):",f6)));
        tb5.addCell(new PdfPCell(new Phrase("Código del desecho:",f6)));
        tb5.addCell(new PdfPCell(new Phrase("CONTENEDOR",f6)));

        /*PdfPTable tb12 =  new PdfPTable(2);
        tb12.addCell(new Phrase("TIPO",f6));
        tb12.addCell(new Phrase("CAPACIDAD",f6));
        tb12.completeRow();
        _cell = new PdfPCell(tb12);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);*/

        tb5.addCell(new PdfPCell(new Phrase("CANTIDAD TOTAL DEL DESECHO:",f6)));
        tb5.addCell(new PdfPCell(new Phrase("UNIDAD VOLUMEN/PESO:",f6)));
        tb5.setHorizontalAlignment(1);
        tb5.completeRow();

        _cell = new PdfPCell(tb5);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 6
        PdfPTable tb6 = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(createGeneradorDetalle(f6));
        cell.setPadding(2.0f);
        cell.setMinimumHeight(60f);
        tb6.addCell(cell);
        tb6.completeRow();

        _cell = new PdfPCell(tb6);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 7
        PdfPTable tb7 = new PdfPTable(new float[] { 100});
        tb7.addCell(new PdfPCell(new Phrase("9. INSTRUCCIONES ESPECIALES E INFORMACIÓN ADICIONAL PARA EL MANEJO SEGURO (INDICAR INCOMPATIBILIDAD):",f6)));
        tb7.completeRow();

        _cell = new PdfPCell(tb7);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 8
        PdfPTable tb8 = new PdfPTable(new float[] { 100});
        tb8.addCell(new PdfPCell(new Phrase("10. CERTIFICACIÓN DEL GENERADOR:\n" +
                "DECLARO QUE EL CONTENIDO DE ESTE LOTE ESTA TOTAL Y CORRECTAMENTE DESCRITO MEDIANTE EL NOMBRE DEL DESECHO, CARACTERÍSTICAS CRTIB, BIEN EMPACADO, ENVASADO MARCADO Y ROTULADO, NO ESTÁ MEZCLADO CON DESECHOS O MATERIALES INCOMPATIBLES SE HAN PREVISTO LAS CONDICIONES DE SEGURIDAD PARA SU TRANSPORTE POR VÍA TERRESTRE DE ACUERDO A LA LEGISLACIÓN NACIONAL VIGENTE.",f6)));
        tb8.completeRow();
        _cell = new PdfPCell(tb8);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 9


        PdfPTable tb9 = new PdfPTable(3);
        Paragraph para2 = new Paragraph();
        para2.add(new Chunk("NOMBRE, CARGO Y FIRMA DEL RESPONSABLE:",f6));
        para2.add(Chunk.NEWLINE);
        para2.add(new Chunk("TELÉFONO Y/O CORREO ELECTRÓNICO DE RESPONSABLE:",f6));


        cell =  new PdfPCell(para2);
        cell.setBorder(Rectangle.NO_BORDER);
        tb9.addCell(cell);

        Paragraph para3 = new Paragraph();
        if(tecnicoEntity!=null) {
            para3.add(new Chunk(tecnicoEntity.getNombre() + "   " + tecnicoEntity.getIdentificacion(), f6));
            para3.add(Chunk.NEWLINE);
            para3.add(new Chunk(tecnicoEntity.getTelefono() + "   " + tecnicoEntity.getCorreo(), f6));
            para3.add(Chunk.NEWLINE);

        }else{

            para3.add(new Chunk("", f6));
            para3.add(Chunk.NEWLINE);
            para3.add(new Chunk("", f6));
            para3.add(Chunk.NEWLINE);
        }
        if(!manifiesto.getTecnicoCorreo().equals("")){
            para3.add(new Chunk(","+manifiesto.getTecnicoCorreo(),f6));
        }
        if(!manifiesto.getCorreoAlterno().equals("")){
            para3.add(new Chunk("," + manifiesto.getCorreoAlterno(),f6));
        }

        //cell = new PdfPCell(new Phrase(manifiesto.getTecnicoResponsable(),f6));
        cell =  new PdfPCell(para3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        tb9.addCell(cell);

        //tb9.addCell(new PdfPCell(new Phrase(manifiesto.getTecnicoTelefono(),f6)));
        if(fileFirmaTecnico!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            firma.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image jpg = Image.getInstance(stream.toByteArray());
            jpg.scaleToFit(40,150);
            cell = new PdfPCell(createCell_ImagenFirma_NO_BORDER(jpg,f6,Element.ALIGN_CENTER));
        }else {

            cell = new PdfPCell(createCell_NO_BORDER("",f6,Element.ALIGN_CENTER));
        }

        tb9.addCell(cell);

        tb9.completeRow();
        _cell = new PdfPCell(tb9);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);




        //tabla 10
        PdfPTable tb10 = new PdfPTable(new float[] { 50,20,30});
        tb10.addCell(new PdfPCell(new Phrase("NO. DE RESOLUTIVO DE NO REUSO/RECICLAJE EN LA INSTALACIÓN.:",f6)));
        tb10.addCell(new PdfPCell(new Phrase("FECHA:",f6)));
        tb10.addCell(new PdfPCell(new Phrase(fecha,f6)));
        tb10.completeRow();

        _cell = new PdfPCell(tb10);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tb.getDefaultCell().setBorderWidth(0f);
        return tb;
    }


    //PDF MANIFIESTO TRANSPORTE
    private PdfPTable createGeneradorTran(Font f6) throws Exception {

        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);

        //tabla 1
        PdfPTable tb1 = new PdfPTable(1);
        tb1.addCell(new PdfPCell(new Phrase("11. NOMBRE DE LA EMPRESA TRANSPORTISTA:         GADERE S.A",f6)));
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 2
        PdfPTable tb2 = new PdfPTable(new float[] { 10,90});
        tb2.addCell(new PdfPCell(new Phrase("DOMICILIO:",f6)));
        tb2.addCell(new PdfPCell(new Phrase("GUAYAQUIL: Cdla. Santa Leonor Mz. 5 Solar 17 - Av. Benjamín Rosales\n"+
                "QUITO  : Av. Naciones Unidas y Amazonas - Edif. La Previsora, Torre B Of. 408\n"+
                "CUENCA   : Parque Industrial. Calle Primera y Carlos Tosi",f6)));
        tb2.completeRow();

        _cell = new PdfPCell(tb2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 3
        PdfPTable tb3 = new PdfPTable(new float[] { 25,25,25,25});
        tb3.addCell(new PdfPCell(new Phrase("                    TELF. GUAYAQUIL\n" +
                "                       46050050 \n" +
                "   TELF. QUITO            TELF. CUENCA\n" +
                "     26015070                  72814991 \n",f6)));
        tb3.addCell(new PdfPCell(new Phrase("NO. DE LICENCIA AMBIENTAL DEL MAE:\n" +
                " Resolución N° 118 -\n" +
                "Diciembre/2004",f6)));
        tb3.addCell(new PdfPCell(new Phrase("NO. DE LICENCIA DE POLICÍA NACIONAL.\n" +
                " ",f6)));
        tb3.addCell(new PdfPCell(new Phrase("NO. DE PLAN DE CONTINGENCIAS APROBADO:\n" +
                " ",f6)));
        tb3.completeRow();

        _cell = new PdfPCell(tb3);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 4
        PdfPTable tb4 = new PdfPTable(new float[] { 25,25,50});
        tb4.addCell(new PdfPCell(new Phrase("Si el desecho se exporta, indicar\n" +
                "    NO APLICA",f6)));
        tb4.addCell(new PdfPCell(new Phrase("No de embarque:\n" +
                "    NO APLICA",f6)));
        tb4.addCell(new PdfPCell(new Phrase("Puerto de salida:   NO APLICA          Fecha:   NO APLICA  \n" +
                "Autorización:   NO APLICA",f6)));
        tb4.completeRow();

        _cell = new PdfPCell(tb4);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 5
        PdfPTable tb5 = new PdfPTable(1);
        tb5.addCell(new PdfPCell(new Phrase("12. RECIBÍ LOS DESECHOS DESCRITOS EN EL MANIFIESTO PARA SU TRANSPORTE. ",f6)));

        _cell = new PdfPCell(tb5);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tab 10 firmas
        PdfPTable tb10 = new PdfPTable(7);
        tb10.addCell(createCell_VACIO());
        Image jpg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(firmaTransportista!=null){
            firmaTransportista.compress(Bitmap.CompressFormat.PNG, 100, stream);
            jpg = Image.getInstance(stream.toByteArray());
            jpg.scaleToFit(70,150);
            tb10.addCell((createCell_ImagenFirma_NO_BORDER(jpg,f6,Element.ALIGN_CENTER)));
        }else{
            if (firmaNoRecoleccion!=null){
                firmaNoRecoleccion.compress(Bitmap.CompressFormat.PNG,100,stream);
                jpg = Image.getInstance(stream.toByteArray());
                jpg.scaleToFit(70,150);
                tb10.addCell((createCell_ImagenFirma_NO_BORDER(jpg,f6,Element.ALIGN_CENTER)));
            }else{
                tb10.addCell(createCell_VACIO());
            }

        }
        tb10.addCell(createCell_VACIO());
        Image operador;
        ByteArrayOutputStream streamO = new ByteArrayOutputStream();
        if(firmaOperador1!=null){
            firmaOperador1.compress(Bitmap.CompressFormat.PNG, 100, streamO);
            operador = Image.getInstance(streamO.toByteArray());
            operador.scaleToFit(70,150);
            tb10.addCell((createCell_ImagenFirma_NO_BORDER(operador,f6,Element.ALIGN_CENTER)));
        }else{
            if (firmaOperador1NoRecoleccion!=null){
                firmaOperador1NoRecoleccion.compress(Bitmap.CompressFormat.PNG,100,streamO);
                operador = Image.getInstance(streamO.toByteArray());
                operador.scaleToFit(70,150);
                tb10.addCell((createCell_ImagenFirma_NO_BORDER(operador,f6,Element.ALIGN_CENTER)));
            }else{
                tb10.addCell(createCell_VACIO());
            }

            //cell = new PdfPCell(createCell_NO_BORDER("",f6,Element.ALIGN_CENTER));
        }
        tb10.addCell(createCell_VACIO());

        Image operador2;
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        if(firmaOperador2!=null){
            firmaOperador2.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            operador2 = Image.getInstance(stream1.toByteArray());
            operador2.scaleToFit(70,150);
            tb10.addCell((createCell_ImagenFirma_NO_BORDER(operador2,f6,Element.ALIGN_CENTER)));
        }else{
            if (firmaOperador2NoRecoleccion!=null){
                firmaOperador2NoRecoleccion.compress(Bitmap.CompressFormat.PNG,100,stream1);
                operador2 = Image.getInstance(stream1.toByteArray());
                operador2.scaleToFit(70,150);
                tb10.addCell((createCell_ImagenFirma_NO_BORDER(operador2,f6,Element.ALIGN_CENTER)));
            }else{
                tb10.addCell(createCell_NO_BORDER("",f6,Element.ALIGN_CENTER));
            }

            //cell = new PdfPCell(createCell_NO_BORDER("",f6,Element.ALIGN_CENTER));
        }
       // tb10.addCell(new PdfPCell(new Phrase("")));
        Paragraph para8 = new Paragraph();
        para8.add(new Chunk("FECHA EMBARQUE:",f6));
        para8.add(Chunk.NEWLINE);
        para8.add(new Chunk(fecha,f6));
        para8.add(Chunk.NEWLINE);
        para8.add(new Chunk("Dia  Mes  Año",f6));
        para8.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para8);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb10.addCell(_cell);

        tb10.completeRow();
        _cell = new PdfPCell(tb10);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 6
        PdfPTable tb6 = new PdfPTable(7);

        Paragraph para1 = new Paragraph();

        para1.add(new Chunk("NOMBRE:",f6));
        para1.add(Chunk.NEWLINE);
        para1.add(new Chunk("CI:",f6));
        para1.add(Chunk.NEWLINE);
        para1.add(new Chunk("CONDUCTOR DE RECOLECCIÓN",f6));
        para1.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para2 = new Paragraph();
        para2.add(new Chunk(manifiesto.getConductorNombre(),f6));
        para2.add(Chunk.NEWLINE);
        if(manifiesto.getConductorIdentificacion()!= null){
            para2.add(new Chunk(manifiesto.getConductorIdentificacion(),f6));
            para2.add(Chunk.NEWLINE);
        }else{
            para2.add(new Chunk("",f6));
            para2.add(Chunk.NEWLINE);
        }
        _cell = new PdfPCell(para2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para3 = new Paragraph();
        para3.add(new Chunk("NOMBRE:",f6));
        para3.add(Chunk.NEWLINE);
        para3.add(new Chunk("CI:",f6));
        para3.add(Chunk.NEWLINE);
        para3.add(new Chunk("AUXILIAR DE RECOLECCIÓN",f6));
        para3.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para3);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para4 = new Paragraph();
        if(manifiesto.getAuxiliarNombre()!=null){
            para4.add(new Chunk(manifiesto.getAuxiliarNombre(),f6));
        }else {
            para4.add(new Chunk("",f6));
        }
        para4.add(Chunk.NEWLINE);
        if(manifiesto.getAuxiliarIdentificacion()!=null){
            para4.add(new Chunk(manifiesto.getAuxiliarIdentificacion(),f6));
        }else{
            para4.add(new Chunk("",f6));
        }

        para4.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para4);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para6 = new Paragraph();
        para6.add(new Chunk("NOMBRE:",f6));
        para6.add(Chunk.NEWLINE);
        para6.add(new Chunk("CI:",f6));
        para6.add(Chunk.NEWLINE);
        para6.add(new Chunk("AUXILIAR DE RECOLECCIÓN",f6));
        para6.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para6);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para7 = new Paragraph();
        if(manifiesto.getNombreOperadorRecolector()!=null){
            para7.add(new Chunk(manifiesto.getNombreOperadorRecolector(),f6));
        }else {
            para7.add(new Chunk("",f6));
        }
        para7.add(Chunk.NEWLINE);
        if(manifiesto.getIdentificacionOperadorRecolector()!=null){
            para7.add(new Chunk(manifiesto.getIdentificacionOperadorRecolector(),f6));
        }else {
            para7.add(new Chunk("",f6));
        }

        para7.add(Chunk.NEWLINE);
        _cell = new PdfPCell(para7);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);

        Paragraph para5 = new Paragraph();
        //para5.add(new Chunk("FIRMA FIRMA:",f6));
        para5.add(new Chunk("",f6));
        para5.add(Chunk.NEWLINE);
        //para5.add(new Chunk(fecha,f6));
        _cell = new PdfPCell(para5);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb6.addCell(_cell);


        tb6.completeRow();

        _cell = new PdfPCell(tb6);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        PdfPTable tb6_1= new PdfPTable(1);

        Paragraph para6_1= new Paragraph();
        //para5.add(new Chunk("FIRMA FIRMA:",f6));
        para6_1.add(new Chunk("AUXILIAR DE RECOLECCIÓN                AUXILIAR DE RECOLECCIÓN          AUXILIAR DE RECOLECCIÓN",f6));
        para6_1.add(Chunk.NEWLINE);
        //para5.add(new Chunk(fecha,f6));
        _cell = new PdfPCell(para6_1);
        _cell.setBorder(Rectangle.NO_BORDER);


        tb6_1.completeRow();
        tb6.addCell(_cell);


        _cell = new PdfPCell(tb6_1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 7
        PdfPTable tb7 = new PdfPTable(new float[] { 100});
        tb7.addCell(new PdfPCell(new Phrase("13. RUTA DE LA EMPRESA GENERADORA HASTA SU ENTREGA.",f6)));
        tb7.completeRow();

        _cell = new PdfPCell(tb7);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 8
        PdfPTable tb8 = new PdfPTable(new float[] { 30,20,30,20});
        tb8.addCell(new PdfPCell(new Phrase("PROVINCIA, CANTÓN Y PARROQUIAS INTERMEDIAS",f6)));
        tb8.addCell(new PdfPCell(new Phrase("",f6)));
        tb8.addCell(new PdfPCell(new Phrase("CARRETERAS O CAMINOS UTILIZADOS",f6)));
        tb8.addCell(new PdfPCell(new Phrase("",f6)));
        tb8.completeRow();

        _cell = new PdfPCell(tb8);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 9
        PdfPTable tb9 = new PdfPTable(new float[] { 25,25,25,25});
        tb9.addCell(new PdfPCell(new Phrase("14. TIPO DE VEHÍCULO",f6)));
        tb9.addCell(new PdfPCell(new Phrase(catalogo.getNombre(),f6)));
        tb9.addCell(new PdfPCell(new Phrase("No. DE PLACA:",f6)));
        tb9.addCell(new PdfPCell(new Phrase(manifiesto.getNumeroPlacaVehiculo(),f6)));
        tb9.completeRow();

        _cell = new PdfPCell(tb9);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);
        //tb.getDefaultCell().setBorderWidth(0f);
        return tb;

    }

    //PDF MANIFIESTO DESTINATARIO
    private PdfPTable createGeneradorDestinatario(Font f6) throws Exception {

        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);

        //tabla 1
        PdfPTable tb1 = new PdfPTable(new float[] { 30,70});
        tb1.addCell(new PdfPCell(new Phrase("15. NOMBRE DE LA EMPRESA DESTINATARIA:",f6)));
        tb1.addCell(createCell("GADERE S.A",f6));
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 2
        PdfPTable tb2 = new PdfPTable(new float[] { 30,70});
        tb2.addCell(new PdfPCell(new Phrase("15.1 NÚMERO DE LICENCIA AMBIENTAL:",f6)));
        tb2.addCell(createCell("Resolución N° 118 - Diciembre/2004",f6));
        tb2.completeRow();

        _cell = new PdfPCell(tb2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 3
        PdfPTable tb3 = new PdfPTable(new float[] { 30,70});
        tb3.addCell(new PdfPCell(new Phrase("       DOMICILIO:",f6)));
        tb3.addCell(createCell("Km. 30 de la vía a Daule, comuna P",f6));
        tb3.completeRow();

        _cell = new PdfPCell(tb3);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 4

        PdfPTable tb4 = new PdfPTable(1);
        tb4.addCell(new PdfPCell(new Phrase("15.2 En caso de existir diferencias en la Verificación de entrega (Marcar con una X) :",f6)));
        tb4.completeRow();

        _cell = new PdfPCell(tb4);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 5

        PdfPTable tb5 = new PdfPTable(1);

        PdfPCell cell = new PdfPCell(createVerificacion(f6));
        tb5.addCell(cell);
        tb5.completeRow();

        _cell = new PdfPCell(tb5);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 6
        PdfPTable tb6 = new PdfPTable(1);
        tb6.addCell(new PdfPCell(new Phrase("15.3 Destinatario alterno:      Nombre:",f6)));
        tb6.completeRow();

        _cell = new PdfPCell(tb6);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 7
        PdfPTable tb7 = new PdfPTable(2);
        tb7.addCell(new PdfPCell(new Phrase("Telefono: ",f6)));
        tb7.addCell(new PdfPCell(new Phrase("No. de Licencia Ambiental: ",f6)));
        tb7.completeRow();

        _cell = new PdfPCell(tb7);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 8
        PdfPTable tb8 = new PdfPTable(new float[] { 40,30,15,5,5,5});

        tb8.addCell(new PdfPCell(new Phrase("15.4 Nombre y Firma del responsable del destinatario alterno",f6)));
        tb8.addCell(new PdfPCell(new Phrase("",f6)));
        tb8.addCell(new PdfPCell(new Phrase("FECHA",f6)));
        tb8.addCell(new PdfPCell(new Phrase("Dia",f6)));
        tb8.addCell(new PdfPCell(new Phrase("Mes",f6)));
        tb8.addCell(new PdfPCell(new Phrase("Año",f6)));
        tb8.completeRow();

        _cell = new PdfPCell(tb8);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 9
        PdfPTable tb9 = new PdfPTable(new float[] { 25,10,10,15,10,15,5});
        tb9.addCell (new PdfPCell(new Phrase("15.5 MANEJO QUE SE DARÁ AL DESECHO\n"+ "(Indicar con X y o especificar)" ,f6)));
        tb9.addCell(new PdfPCell(new Phrase("REUSO/RECICLAJE\n"+"_________________",f6)));
        tb9.addCell(new PdfPCell(new Phrase("TRATAMIENTO\n"+"_________________",f6)));
        tb9.addCell(new PdfPCell(new Phrase("CO-PROCESAMIENTO\n"+"________________________",f6)));
        tb9.addCell(new PdfPCell(new Phrase("INCINERACIÓN\n"+"__________________",f6)));
        tb9.addCell(new PdfPCell(new Phrase("RELLENO DE SEGURIDAD\n"+"_________________________",f6)));
        tb9.addCell(new PdfPCell(new Phrase("OTROS\n"+"_______",f6)));
        tb9.completeRow();

        _cell = new PdfPCell(tb9);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);
        //tb.getDefaultCell().setBorderWidth(0f);

        //tabla 10
        PdfPTable tb10 = new PdfPTable(new float[] { 1});
        tb10.addCell (new PdfPCell(new Phrase("16. CERTIFICACIÓN DE LA RECEPCIÓN DE LOS DESECHOS DESCRITOS EN EL MANIFIESTO INDICADOS EN EL MANIFIESTO EXCEPTO LO INDICADO EN EL PUNTO 12)\n"+"." ,f6)));
        tb10.completeRow();

        _cell = new PdfPCell(tb10);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 11
        PdfPTable tb11 = new PdfPTable(new float[] { 1});
        tb11.addCell (new PdfPCell(new Phrase("OBSERVACIONES:  " ,f6)));
        tb11.completeRow();

        _cell = new PdfPCell(tb11);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 12
        PdfPTable tb12 = new PdfPTable(new float[] { 1});
        tb12.addCell (new PdfPCell(new Phrase("." ,f6)));
        tb12.completeRow();

        _cell = new PdfPCell(tb12);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 13
        PdfPTable tb13 = new PdfPTable(new float[] { 50,20,30});
        tb13.addCell (new PdfPCell(new Phrase("NOMBRE: \n\n"+"CARGO: " ,f6)));
        tb13.addCell (new PdfPCell(new Phrase("FECHA DE RECEPCIÓN: \n"+ "\n"+ "_____________________\n"+"Dia"+
                "      Mes"+
                "           Año",f6)));

        tb13.addCell(new PdfPCell(new Phrase("FIRMA\n\n"
                + "NOMBRE: ",f6)));
        tb13.completeRow();

        _cell = new PdfPCell(tb13);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        return tb;

    }


    //PDF MANIFIESTO INFORMACION ADICIONAL
    private PdfPTable createGeneradorInfoAdi(Font f6)throws Exception {

        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);
        //tabla 1
        PdfPTable tb1 = new PdfPTable(2);
        PdfPCell cell2 = new PdfPCell(ObservacionesResiduos(f6));
        PdfPCell cell = new PdfPCell(ObservacionesFrecuentes(f6));
        /*cell2.setPadding(2.0f);
        cell2.setMinimumHeight(60f);
        cell.setPadding(2.0f);
        cell.setMinimumHeight(60f);*/

        tb1.addCell(cell2);
        tb1.addCell(cell);
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        return tb;

    }

    private PdfPTable ObservacionesResiduos(Font f3) throws Exception{
        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);
        //tabla 1
        PdfPTable tb1 = new PdfPTable(new float[] { 100});
        tb1.addCell(new PdfPCell(new Phrase("ESPACIO EXCLUSIVO PARA MANIFIESTOS DE\n" +
                "RECOLECCION DE RESIDUOS\n"+
                "BIOLOGICOS INFECCIOSOS",f3)));
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 2
        PdfPTable tb2 = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(regionAdicionales(f3));
        PdfPCell cell2 = new PdfPCell(regionAdicionales2(f3));
        PdfPCell cell3 = new PdfPCell(regionAdicionales3(f3));
        //tb2.addCell(new PdfPCell(new Phrase("PC-4",f3)));
       /* tb2.addCell(new PdfPCell(new Phrase("0",f3)));
        tb2.addCell(new PdfPCell(new Phrase("PC1",f3)));
        tb2.addCell(new PdfPCell(new Phrase("0",f3)));
        tb2.addCell(new PdfPCell(new Phrase("FUNDA 63 * 76",f3)));
        tb2.addCell(new PdfPCell(new Phrase("0",f3)));
        tb2.addCell(new PdfPCell(new Phrase("PC2",f3)));
        tb2.addCell(new PdfPCell(new Phrase("0",f3)));
        tb2.completeRow();*/
        tb2.addCell(cell);
        tb2.addCell(cell2);
        tb2.addCell(cell3);
        tb2.completeRow();


        _cell = new PdfPCell(tb2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 3
        PdfPTable tb3 = new PdfPTable(new float[] { 20,70,10});
        tb3.addCell(new PdfPCell(new Phrase("OTRO",f3)));
        tb3.addCell(new PdfPCell(new Phrase("_______________________________________________ ",f3)));
        tb3.addCell(new PdfPCell(new Phrase("0",f3)));
        tb3.completeRow();

        _cell = new PdfPCell(tb3);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 4
        PdfPTable tb4 = new PdfPTable(new float[] { 100});
        tb4.addCell(new PdfPCell(new Phrase("OTRAS OBSERVACIONES ENCONTRADAS POR EL CLIENTE",f3)));
        tb4.completeRow();

        _cell = new PdfPCell(tb4);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla n
        PdfPTable tbn = new PdfPTable(1);
        PdfPCell celln = new PdfPCell(createOtrasNovedades(f3));
        //celln.setPadding(2.0f);
        //celln.setMinimumHeight(60f);
        tbn.addCell(celln);
        tbn.completeRow();

        _cell = new PdfPCell(tbn);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);


        //tabla 5
        PdfPTable tb5 = new PdfPTable(new float[] { 100});
        Image jpg = null;




        tb5.addCell(new PdfPCell(new Phrase(" \n"+" \n"+" \n"+" \n"+" \n"+" \n"+" \n"+" \n"+
                " \n"+" \n"+" \n"+" \n"+" \n"+" \n"+" \n"+" \n"+"\n",f3)));
        if(listaPaquetes!=null && listaPaquetes.size()>0) {
            tb5.addCell(new Phrase("PENDIENTES GUARDIANES  "+ pg + "\n"
                                        +"PENDIENTES FUNDA  "+ pf,f3));
        }
        if(fileFirmaTecnico!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            firma.compress(Bitmap.CompressFormat.PNG, 100, stream);
            jpg = Image.getInstance(stream.toByteArray());
            jpg.scaleToFit(40,150);
            tb5.addCell(new PdfPCell(createCell_ImagenFirma_NO_BORDER(jpg,f3,Element.ALIGN_CENTER)));
        }else {

            tb5.addCell(new PdfPCell(createCell_NO_BORDER("",f3,Element.ALIGN_CENTER)));
        }

        tb5.addCell(new PdfPCell(new Phrase("                                                                           FIRMA\n"+
                "DECLARO CONFORMIDAD DE LA INFORMACION ADICIONAL",f3)));
        tb5.completeRow();

        _cell = new PdfPCell(tb5);
        _cell.setBorder(Rectangle.NO_BORDER);

        tb.addCell(_cell);



        return tb;

    }

    private PdfPTable ObservacionesFrecuentes(Font f3) throws Exception {

        PdfPCell _cell;
        PdfPTable tb = new PdfPTable(1);

        //tabla 1
        PdfPTable tb1 = new PdfPTable(new float[] { 100});
        tb1.addCell(new PdfPCell(new Phrase("OBSERVACIONES FRECUENTES",f3)));
        tb1.completeRow();

        _cell = new PdfPCell(tb1);
        _cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        //tabla 2
        PdfPTable tb2 = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(createObservacionesFrecuentes(f3));
        cell.setPadding(2.0f);
        cell.setMinimumHeight(60f);
        tb2.addCell(cell);
        tb2.completeRow();

        _cell = new PdfPCell(tb2);
        _cell.setBorder(Rectangle.NO_BORDER);
        tb.addCell(_cell);

        return tb;
    }

    private PdfPCell createCell(String texto, Font f6){
        PdfPCell _cell = new PdfPCell(new Phrase(texto,f6));
        _cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //_cell.setBorder(Rectangle.NO_BORDER);
        return _cell;
    }

    private PdfPCell createCell_NO_BORDER(String texto, Font f6, Integer ALIGN){
        PdfPCell _cell = new PdfPCell(new Phrase(texto,f6));
        _cell.setBorder(Rectangle.NO_BORDER);
        _cell.setFixedHeight(8f);
        _cell.setPadding(1f);
        if(ALIGN!=null)_cell.setHorizontalAlignment(ALIGN);
        return _cell;
    }

    private PdfPCell createCellD_NO_BORDER(Double texto, Font f6, Integer ALIGN){
        PdfPCell _cell = new PdfPCell(new Phrase(texto.toString(),f6));
        _cell.setBorder(Rectangle.NO_BORDER);
        _cell.setFixedHeight(8f);
        _cell.setPadding(1f);
        if(ALIGN!=null)_cell.setHorizontalAlignment(ALIGN);
        return _cell;
    }

    private PdfPCell createCell_Imagen_NO_BORDER(Image jpg, Font f6, Integer ALIGN){

        PdfPCell _cell = new PdfPCell(jpg);
        _cell.setBorder(Rectangle.NO_BORDER);
        _cell.setFixedHeight(8f);
        _cell.setPadding(1f);
        if(ALIGN!=null)_cell.setHorizontalAlignment(ALIGN);
        return _cell;
    }
    private PdfPCell createCell_ImagenFirma_NO_BORDER(Image jpg, Font f6, Integer ALIGN){

        PdfPCell _cell = new PdfPCell(jpg);
        _cell.setBorder(Rectangle.NO_BORDER);
        _cell.setFixedHeight(30f);
        _cell.setPadding(1f);
        if(ALIGN!=null)_cell.setHorizontalAlignment(ALIGN);
        return _cell;
    }

    private PdfPCell createCell_NO_BORDER_SINGLE(String texto, Font f6, Integer ALIGN ){
        PdfPCell _cell = new PdfPCell(new Phrase(new Chunk(texto,f6)));
        _cell.setBorder(Rectangle.NO_BORDER);
        //_cell.setNoWrap(true);
        _cell.setFixedHeight(8f);
        _cell.setPadding(1f);
        if(ALIGN!=null)_cell.setHorizontalAlignment(ALIGN);
        //_cell.setVerticalAlignment(Element.ALIGN_TOP);

        return _cell;
    }

    private PdfPCell createCell_VACIO(){
        PdfPCell _cell = new PdfPCell(new Phrase(new Chunk("")));
        _cell.setBorder(Rectangle.NO_BORDER);
        _cell.setPadding(1f);
        return _cell;
    }
    private PdfPTable regionAdicionales(Font f3)  {

        PdfPTable det = new PdfPTable(new float[]{10,10,10,10,10});

        if(listaPaquetes!=null && listaPaquetes.size()>0){
            det.addCell(new PdfPCell(new Phrase("Funda 55x50", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(fundas50, f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));

            det.addCell(new PdfPCell(new Phrase("PC-1", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(paquetes1, f3, null));
        }else {

            det.addCell(new PdfPCell(new Phrase("Funda 55x50", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(" ", f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));

            det.addCell(new PdfPCell(new Phrase("PC-1", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(" ", f3, null));
        }

        det.completeRow();

        return det;
    }
    private PdfPTable regionAdicionales2(Font f3)  {

        PdfPTable det = new PdfPTable(new float[]{10,10,10,10,10});

        if(listaPaquetes!=null && listaPaquetes.size()>0){
            det.addCell(new PdfPCell(new Phrase("Funda 63x76", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(fundas63, f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));

            det.addCell(new PdfPCell(new Phrase("PC-2", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(paquete2, f3, null));
        }else {
            det.addCell(new PdfPCell(new Phrase("Funda 63x76", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(" ", f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));

            det.addCell(new PdfPCell(new Phrase("PC-2", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(" ", f3, null));

        }
        det.completeRow();

        return det;
    }
    private PdfPTable regionAdicionales3(Font f3)  {

        PdfPTable det = new PdfPTable(new float[]{10,10,10,10,10});

        if(listaPaquetes!=null && listaPaquetes.size()>0){
            det.addCell(new PdfPCell(new Phrase("PC-4", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(paquete3, f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));

        }else {
            det.addCell(new PdfPCell(new Phrase("PC-4", f3)));
            det.addCell(createCell_NO_BORDER_SINGLE(" ", f3, null));

            det.addCell(new PdfPCell(new Phrase("", f3)));


        }
        det.completeRow();

        return det;
    }

    private PdfPTable createObservacionesFrecuentes(Font f3) throws Exception {

        //dbHelper.open();
        List<RowItemHojaRutaCatalogo> novedades = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuente(idManifiesto);
        //dbHelper.close();

        PdfPTable det = new PdfPTable(new float[]{10,80});
        det.setWidthPercentage(100);

        if(novedades.size()>0) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagenCheck.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image jpgCheck = Image.getInstance(stream.toByteArray());
            jpgCheck.scaleToFit(16,16);
            stream.close();

            stream = new ByteArrayOutputStream();
            imagenUnCheck.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image jpgUnCheck = Image.getInstance(stream.toByteArray());
            jpgUnCheck.scaleToFit(16,16);
            stream.close();


            for(RowItemHojaRutaCatalogo ct:novedades){
                if(ct.isEstadoChek()){
                    det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));
                }else{
                    det.addCell(createCell_Imagen_NO_BORDER(jpgUnCheck,f3,Element.ALIGN_CENTER));
                }
                det.addCell(createCell_NO_BORDER_SINGLE(ct.getCatalogo(), f3,null));
            }

            stream.close();
        }

        det.addCell(createCell_VACIO());
        det.addCell(createCell_VACIO());
        det.completeRow();

        return det;

    }


    private PdfPTable createOtrasNovedades(Font f3) throws Exception {

        String novedadesEncontradas = manifiesto.getNovedadEncontrada();

        PdfPTable det = new PdfPTable(1);
        det.setWidthPercentage(100);

        if(manifiesto.getNovedadEncontrada()!= null){
            det.addCell(createCell_NO_BORDER_SINGLE(novedadesEncontradas,f3,Element.ALIGN_CENTER));
        }else{
            det.addCell(createCell_VACIO());
        }



        det.addCell(createCell_VACIO());
        det.addCell(createCell_VACIO());
        det.completeRow();

        return det;

    }
    private PdfPTable createVerificacion(Font f3) throws Exception {

        PdfPTable det = new PdfPTable(new float[]{10,10,10,10,10,10,10,10,10,10});
        det.setWidthPercentage(100);

        String nombre;
        int pos=-1;

        //InputStream ims = context.getAssets().open("ic_uncheck.png");
        //Bitmap bmp = BitmapFactory.decodeStream(ims);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagenUnCheck.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image jpgCheck = Image.getInstance(stream.toByteArray());
        jpgCheck.scaleToFit(16,16);
        stream.close();
        //ims.close();

        det.addCell(createCell_NO_BORDER_SINGLE("Cantidad: ",f3,null));
        det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));

        det.addCell(createCell_NO_BORDER_SINGLE("Tipo: ",f3,null));
        det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));

        det.addCell(createCell_NO_BORDER_SINGLE("Desecho: ",f3,null));
        det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));

        det.addCell(createCell_NO_BORDER_SINGLE("Rechazo parcial: ",f3,null));
        det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));

        det.addCell(createCell_NO_BORDER_SINGLE("Rechazo total: ",f3,null));
        det.addCell(createCell_Imagen_NO_BORDER(jpgCheck,f3,Element.ALIGN_CENTER));


        stream.close();
        //ims.close();


        det.addCell(createCell_VACIO());
        det.addCell(createCell_VACIO());
        det.completeRow();
        //det.setLockedWidth(true);


        return det;

    }

    private PdfPTable createGeneradorDetalle(Font f6){

        //dbHelper.open();
        List<RowItemManifiesto> detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idManifiesto);
        //dbHelper.close();

        PdfPTable det = new PdfPTable(new float[]{40, 20, 10, 10,10,10});
        det.setWidthPercentage(100);
        /*
        PdfPTable det = new PdfPTable(4);
        try {
            det.setTotalWidth(new float[]{ 200,40,20,20 });
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        */
        if(detalles.size()>0) {

            //cursor.moveToFirst();
            String nombre;
            int pos=-1;
            for (RowItemManifiesto reg:detalles){
                nombre = reg.getDescripcion();
                pos = nombre.indexOf("-");
                if(reg.getCantidadBulto()!=0 || reg.getTipoMostrar().toString().equals("3")) {
                    if (pos > 9) {
                        det.addCell(createCell_NO_BORDER_SINGLE(reg.getDescripcion(), f6, null));
                    } else {
                        det.addCell(createCell_NO_BORDER_SINGLE(nombre.substring(pos + 1, nombre.length()), f6, null));
                    }

                    det.addCell(createCell_NO_BORDER_SINGLE(reg.getCodigo(), f6, Element.ALIGN_CENTER));
                    det.addCell(createCell_VACIO());
                    det.addCell(createCell_VACIO());

                    if (reg.getTipoMostrar().toString().equals("3")) {
                        det.addCell(createCell_NO_BORDER("", f6, Element.ALIGN_CENTER));
                        det.addCell(createCell_NO_BORDER("", f6, Element.ALIGN_CENTER));
                    } else {
                        det.addCell(createCellD_NO_BORDER(reg.getCantidadBulto(), f6, Element.ALIGN_CENTER));
                        det.addCell(createCell_NO_BORDER(String.valueOf(reg.getPeso()), f6, Element.ALIGN_CENTER));

                    }
                }
                det.completeRow();
            }
        }

       //det.addCell(createCell_VACIO());
        //det.addCell(createCell_VACIO());
        det.completeRow();
        //det.setLockedWidth(true);


        return det;
    }

    private String getString(String column, Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(column));
    }


    private OutputStream createLocalFile(String nameFile) throws Exception {

        File docsFolder = new File( Environment.getExternalStorageDirectory()+"/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),nameFile);
        return  new FileOutputStream(pdfFile);
    }

    private void loadDataPaquetes(){
        if(idAppTipoPaquete!=null) {
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(idAppTipoPaquete);
            manifiestoPkg = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(idManifiesto, idAppTipoPaquete);
            listaPaquetes = new ArrayList<>();
            if(pkg!=null && manifiestoPkg!=null) {
                if (pkg.getEntregaSoloFundas()) listaPaquetes.add(new RowItemPaquete(pkg.getFunda(),
                        manifiestoPkg != null ? manifiestoPkg.getDatosFundas() : 0,
                        manifiestoPkg != null ? (manifiestoPkg.getDatosFundasPendientes() != null ? manifiestoPkg.getDatosFundasPendientes() : 0) : 0,
                        manifiestoPkg != null ? manifiestoPkg.getDatosFundasPendientes() : null,
                        manifiestoPkg != null ? (manifiestoPkg.getDatosFundasDiferencia() != null ? manifiestoPkg.getDatosFundasDiferencia() : 0) : 0,
                        1));

                if (pkg.getEntregaSoloGuardianes())
                    listaPaquetes.add(new RowItemPaquete(pkg.getGuardian(),
                            manifiestoPkg != null ? manifiestoPkg.getDatosGuardianes() : 0,
                            manifiestoPkg != null ? (manifiestoPkg.getDatosGuardianesPendientes() != null ? manifiestoPkg.getDatosGuardianesPendientes() : 0) : 0,
                            manifiestoPkg != null ? manifiestoPkg.getDatosGuardianesPendientes() : null,
                            manifiestoPkg != null ? (manifiestoPkg.getDatosGuardianesDiferencia() != null ? manifiestoPkg.getDatosGuardianesDiferencia() : 0) : 0,
                            2));
            }
        }
        if(listaPaquetes!=null && listaPaquetes.size()>0) {

            for (RowItemPaquete it : listaPaquetes) {
                listaPaquetes.get(0);
                if (it.getNombre().equals("55x50")) {
                    fundas50 = String.valueOf(it.getCantidad() - it.getPendiente());
                    pf = it.getPendiente().toString();
                }
                if (it.getNombre().equals("63x76")){
                    fundas63 = String.valueOf(it.getCantidad() - it.getPendiente());
                    pf = it.getPendiente().toString();
                }
                if(it.getNombre().equals("PC 1")){
                    paquetes1 = String.valueOf(it.getCantidad() - it.getPendiente());
                    pg = it.getPendiente().toString();
                }else if(it.getNombre().equals("PC 2")) {
                    paquete2 = String.valueOf(it.getCantidad() - it.getPendiente());
                    pg = it.getPendiente().toString();
                }else if(it.getNombre().equals("PC 4")) {
                    paquete3 = String.valueOf(it.getCantidad() - it.getPendiente());
                    pg = it.getPendiente().toString();
                }
            }

        }
        }


}
