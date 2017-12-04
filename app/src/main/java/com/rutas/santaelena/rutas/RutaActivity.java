package com.rutas.santaelena.rutas;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.location.Location;
import android.view.View;
import android.widget.TextView;

public class RutaActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

    }


      /*  Resources res = getResources();

        //String path = Environment.getExternalStorageDirectory().toString() + "/prueba.gpx";
        //System.out.println(path);
        TextView textInfo = (TextView)findViewById(R.id.info);

        String info = "";

        //File gpxFile = new File(path);
        //info += gpxFile.getPath() +"\n\n";
        //System.out.println(gpxFile.toString());
        List<GpxNode> gpxList = decodeGPX(res.openRawResource(R.raw.transcisa7));
        for(int i = 0; i < gpxList.size(); i++){
            info += gpxList.get(i).getLocationString() + "\n";

        }
        System.out.println(info);
        textInfo.setText(info);*/
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.activity_ruta, menu);
        return true;
    }
*/
    /*class GpxNode{

        Location location;
        String ele;
        String time;

        GpxNode(){
            location = null;
            ele = "";
            time = "";
        }

        GpxNode(Location loc){
            location = loc;
            ele = "";
            time = "";
        }

        GpxNode(Location loc, String e, String t){
            location = loc;
            ele = e;
            time = t;
        }

        void setEle(String e){
            ele = e;
        }

        void setTime(String t){
            time = t;
        }

        Location getLocation(){
            return location;
        }

        String getLocationString(){
            return location.getLatitude() + ":" + location.getLongitude();
        }

    }

    private List<GpxNode> decodeGPX(File f)
    {
        List<GpxNode> list = new ArrayList<GpxNode>();
        try {
            FileInputStream fis = new FileInputStream(f);
            list = decodeGPX(fis);
        }catch (FileNotFoundException fne)
        {
            fne.printStackTrace();
        }
        //
        return list;
    }

    private List<GpxNode> decodeGPX(InputStream is){
        List<GpxNode> list = new ArrayList<GpxNode>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //FileInputStream fileInputStream = new FileInputStream();
            Document document = documentBuilder.parse(is);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_trkpt = elementRoot.getElementsByTagName("wpt");

            for(int i = 0; i < nodelist_trkpt.getLength(); i++){

                Node node = nodelist_trkpt.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String newLatitude = attributes.getNamedItem("lat").getTextContent();
                Double newLatitude_double = Double.parseDouble(newLatitude);

                String newLongitude = attributes.getNamedItem("lon").getTextContent();
                Double newLongitude_double = Double.parseDouble(newLongitude);



                String newLocationName = newLatitude + ":" + newLongitude;
                Location newLocation = new Location(newLocationName);
                newLocation.setLatitude(newLatitude_double);
                newLocation.setLongitude(newLongitude_double);



                GpxNode newGpxNode = new GpxNode(newLocation);
                list.add(newGpxNode);

            }

            is.close();

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }*/

