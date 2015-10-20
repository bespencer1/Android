package com.brianspencer.clayshootinglog;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by bspencer on 2/17/14.
 */
public class RoundContent {

    public static List<RoundItem> ITEMS = new ArrayList<RoundItem>();
    public static Map<String, RoundItem> ITEM_MAP = null;
    public static File dataFile = null;
    public static int currentID = 0;

    static {
        ITEM_MAP = new HashMap<String, RoundItem>();
    }

    public static void loadItems(){
        try{
            if(ITEM_MAP.size() == 0){
                try{
                    File file = new File(dataFile, "rounditem");
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                    ITEM_MAP = (Map<String, RoundContent.RoundItem>)inputStream.readObject();
                    inputStream.close();

                    Iterator it = ITEM_MAP.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pairs = (Map.Entry)it.next();
                        RoundItem ri = (RoundItem)pairs.getValue();
                        ITEMS.add(ri);
                        int itID = Integer.parseInt(ri.id);
                        if(itID > currentID)
                            currentID = itID;
                    }

                    sort();

                }
                catch(IOException eIO){
                    eIO.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void sort(){
        Collections.sort(ITEMS);
        /*
        Collections.sort(ITEMS, new Comparator<RoundItem>() {
            @Override
            public int compare(RoundItem roundItem, RoundItem roundItem2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                int retVal =  Integer.getInteger(dateFormat.format(roundItem.shootDate));
                return retVal;
            }
        });
        */
    }

    public static void deleteItem(RoundItem item){
        ITEMS.remove(item);
        ITEM_MAP.remove(item.id);
        saveItems();
    }

    public static void saveItems(){
        try{
            File file = new File(dataFile, "rounditem");
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(ITEM_MAP);
            outputStream.flush();
            outputStream.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        sort();
    }

    public static void addItem(RoundItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        currentID++;
        saveItems();
    }

    public static class RoundItem implements java.io.Serializable, Comparable<RoundItem> {
        public String id;
        public Date shootDate;
        public int roundNum;
        public int hits;
        public int type;

        public RoundItem(String id, Date shootDate, int roundNum, int hits, int type) {
            this.id = id;
            this.shootDate = shootDate;
            this.roundNum = roundNum;
            this.hits = hits;
            this.type = type;
        }

        @Override
        public String toString() {

            String roundType = "";
            switch(this.type){
                case 1:
                    roundType = "Skeet";
                    break;
                case 2:
                    roundType = "Sport";
                    break;
                default:
                    roundType = "Trap ";
                    break;
            }

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            return String.format("%s  %s Round: %s  Hits: %s", dateFormat.format(this.shootDate), roundType, String.valueOf(this.roundNum), String.valueOf(this.hits));
        }

        public int compareTo(RoundItem compareItem){
            int val = this.shootDate.compareTo(compareItem.shootDate);
            if(val == 0){
                return this.roundNum - compareItem.roundNum;
            }
            else
                return val;
        }
    }
}
