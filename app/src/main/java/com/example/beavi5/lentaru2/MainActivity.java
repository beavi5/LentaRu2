package com.example.beavi5.lentaru2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.beavi5.lentaru2.adapters.RSSItem;
import com.example.beavi5.lentaru2.adapters.RVNewsAdapter;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
    ArrayList<RSSItem> arrayListNews;
    RVNewsAdapter rvNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayListNews= new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String feedUrl="https://lenta.ru/rss/articles";
        final StringRequest request=new StringRequest(Request.Method.GET, feedUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                List<RSSItem> rssItems= new ArrayList<>();


                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                try {
                    builder = factory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                InputSource is = new InputSource(new StringReader(response));

                Document document= null;
                try {
                    document = builder.parse(is);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Element element = document.getDocumentElement();
                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength()>0)
                {

                    int count = nodeList.getLength();
                    for (int i = 0; i <nodeList.getLength() ; i++) {
                        Log.d("arr", "" + nodeList.getLength());

                        Element entry = (Element) nodeList.item(i);

                        Element _title = (Element) entry.getElementsByTagName("title").item(0);
                        Element _img = (Element) entry.getElementsByTagName("enclosure").item(0);
                        String img = "";
                        img = (_img != null) ? _img.getAttribute("url") : "";

                        Element _link = (Element) entry.getElementsByTagName("link").item(0);
                        String title = _title.getFirstChild().getNodeValue();
                        //  String img = _img.getFirstChild().getNodeValue();
                        String link = _link.getFirstChild().getNodeValue();


                        String desc = "";

                        Element _desc = (Element) entry.getElementsByTagName("description").item(0);

                        NodeList list = _desc.getChildNodes();

                        for (int index = 0; index < list.getLength(); index++) {
                            if (list.item(index) instanceof CharacterData) {
                                CharacterData child = (CharacterData) list.item(index);
                                desc = child.getData();

                                if (desc != null && desc.trim().length() > 0) {
                                    desc = child.getData();

                                    break;
                                }
                            }
                        }
    arrayListNews.add( new RSSItem(title, desc, img, link));
                            rvNewsAdapter.notifyDataSetChanged();

                    }}
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.rvNews);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        rvNewsAdapter = new RVNewsAdapter(arrayListNews);

        //RSSItem.ParseRss("https://lenta.ru/rss/articles",arrayListNews, rvNewsAdapter);
        recyclerView.setAdapter(rvNewsAdapter);
//        rvNewsAdapter.notifyDataSetChanged();


        requestQueue.add(request);



    }
}
