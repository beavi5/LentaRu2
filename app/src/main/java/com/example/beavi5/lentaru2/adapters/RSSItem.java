package com.example.beavi5.lentaru2.adapters;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.android.volley.toolbox.Volley;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by beavi5 on 20.07.2017.
 */

public class RSSItem {
    String title;
    String desc;
    String img;
    String link;

    public RSSItem(String title, String desc, String img, String link) {
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    public String getLink() {
        return link;
    }


    public static List<RSSItem> ParseRss(String feedUrl, final ArrayList<RSSItem> arrayListNews, final RVNewsAdapter rvNewsAdapter){


new myAsyncTask(arrayListNews,rvNewsAdapter).execute(feedUrl);
        return arrayListNews;
    }



}



class myAsyncTask extends AsyncTask<String,List<RSSItem>,List<RSSItem>>{
  List<RSSItem> arrayListNews;
    RVNewsAdapter rvNewsAdapter;
    public myAsyncTask(List<RSSItem> list,RVNewsAdapter rvNewsAdapter) {
        arrayListNews=list;
        this.rvNewsAdapter=rvNewsAdapter;
    }

    @Override

    protected List<RSSItem> doInBackground(String... strings) {
        List<RSSItem> rssItems= new ArrayList<>();


        try {
            URL url = new URL(strings[0]);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){

                InputStream is = conn.getInputStream();


                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document= db.parse(is);
                
                Element element = document.getDocumentElement();
                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength()>0)
                {

                    int count = nodeList.getLength();
                    for (int i = 0; i <nodeList.getLength() ; i++) {
                        Log.d("arr",""+nodeList.getLength());

                        Element entry = (Element) nodeList.item(i);

                        Element _title= (Element) entry.getElementsByTagName("title").item(0);
                        Element _img= (Element) entry.getElementsByTagName("enclosure").item(0);
                        String img = "";
                        img = ( _img!=null)? _img.getAttribute("url"):"" ;

                        Element _link= (Element) entry.getElementsByTagName("link").item(0);
                       String title = _title.getFirstChild().getNodeValue();
                      //  String img = _img.getFirstChild().getNodeValue();
                        String link = _link.getFirstChild().getNodeValue();



                        String desc="";

                        Element _desc = (Element) entry.getElementsByTagName("description").item(0);

                        NodeList list = _desc.getChildNodes();

                        for(int index = 0; index < list.getLength(); index++){
                            if(list.item(index) instanceof CharacterData){
                                CharacterData child = (CharacterData) list.item(index);
                                desc = child.getData();

                                if(desc != null && desc.trim().length() > 0){
                                    desc= child.getData();

                                    break;}
                            }
                        }


                        rssItems.add( new RSSItem(title, desc, img, link));

                        publishProgress(rssItems);



                    }
                }




            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return rssItems;

    }



    @Override
    protected void onProgressUpdate(List<RSSItem>... values) {
        super.onProgressUpdate(values);
        arrayListNews.clear();
        arrayListNews.addAll(values[0]);
        rvNewsAdapter.notifyDataSetChanged();

    }

}