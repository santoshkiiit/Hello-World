/*
 *  Any use of the Material is governed by the terms of the actual license
 *  agreement between LeanTaaS Inc. and the user.
 *  Copyright 2010 LeanTaaS Inc., LOS GATOS CA USA.
 *  All rights reserved. Any rights not expressly granted herein are
 *  reserved.
 */
package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RestaurantManager {
    
    static RestaurantService restaurantService = new RestaurantService();

    static Map<Integer, Map<String, Double >> combinedMenu ;
    static String searchItem = StringUtils.EMPTY;
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String fileName  = args[0];
       String items =  validateUserRequest(args);
       if(items !=null){
           readMenuFile(fileName,items);
       }

    }

    private static String  validateUserRequest(String[] args) {
        if(args ==null ||args.length==0){
            return null;
        }
        String items ="";
        for(int index =1;index < args.length; index++){
            String item = args[index];
            item =  item.replaceAll("\\s", "").trim();
            items = items + item + ",";
            
        }
        if(items.length() > 1){
            items = items.substring(0,items.length()-1);
        }
       // items = restaurantService.getItemsOrderedByName(items);
        return items;
       
        
    }

    private static void readMenuFile(String fileName, String  items ) throws IOException {
        
     boolean inputCheck =validateMenuFile(fileName);
     if(inputCheck){
         processAndStore(fileName,items);
     }
    }
    
    public static  boolean  validateMenuFile(String fileName) {
        if(fileName==null || fileName.length()<1){
            System.out.println("Input format incorrect");
            return false;
        }else{
            File file =  new File(fileName);
            if(!file.exists()){
                System.out.println("file " +fileName +"doesnot exist");
                return false;
            }
            if(file.length()==0){
                System.out.println("file "+ fileName + "is empty");
                return false;
                
            }
        }
        return true;
    }
    
    public static  void processAndStore(String fileName, String searchString) throws IOException { 
        //reads the input file and generates a hash of hash data structure with outer key as id and inner key as item name and cost as inner value
        BufferedReader bf = new BufferedReader(new FileReader(fileName));
        searchItem = searchString;
        combinedMenu = new HashMap<Integer, Map<String, Double >> ();
        
        String menuItem = "";
        while( (menuItem = bf.readLine()) != null){
        if(menuItem!=null && menuItem.length()> 0){
            String[] menuItemInfo = menuItem.split(",");
            int indexforPrice = menuItem.indexOf(",");
            int indexForItem = menuItem.indexOf(',', indexforPrice+1);
            
            if(menuItemInfo.length > 2){
                Integer restaurantId = Integer.parseInt(menuItemInfo[0]);
                Double  price = Double.parseDouble(menuItemInfo[1]);
                String items = menuItem.substring(indexForItem+1 ,menuItem.length());  
                items =  items.replaceAll("\\s", "").trim();
                items = items.replace(",", "-");
                if(combinedMenu.containsKey(restaurantId)){
                    Map<String, Double>  priceMap = combinedMenu.get(restaurantId);
                    priceMap.put(items, price);
                }else{
                   Map<String, Double>  priceMap = new HashMap<String, Double>();
                   priceMap.put(items, price);
                   combinedMenu.put(restaurantId, priceMap);
                }
            }else{
                System.out.println("record in incorrect format:"+searchString);
            }
        }
        }
        restaurantService.searchForEconomicalRestaurant(combinedMenu,searchItem);
    }


   

    
}
