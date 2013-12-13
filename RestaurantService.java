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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class RestaurantService {
    static Map<Integer, Map<String, Double >> combinedMenu ;
    static String searchItem = StringUtils.EMPTY;

    public  void processAndStore(String fileName, String searchString) throws IOException { 
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
        searchForEconomicalRestaurant(combinedMenu);
    }
    
    private void searchForEconomicalRestaurant(Map<Integer, Map<String, Double>> combinedMenu) {
        Double minPrice = Double.MAX_VALUE;
        Integer cheapestRestaurantId = 0;
        for(Entry<Integer, Map<String, Double>> restaurantList :combinedMenu.entrySet() ){
            Integer restaurantId = restaurantList.getKey();
            Double minCostInRestaurant =  findMinCostinRestaurant(restaurantId);
            if(minCostInRestaurant < minPrice){
                minPrice = minCostInRestaurant;
                cheapestRestaurantId = restaurantId;
            }
        }
        if(cheapestRestaurantId!=0){
            System.out.println(cheapestRestaurantId +","+ minPrice); 
        }else{
            System.out.println("No matching restaurant found");
        }
        
    }


    
    public Double  findMinCostinRestaurant(Integer restaurantId){
        Map<String,Double> priceList  =  combinedMenu.get(restaurantId);
       
        Set<String> itemsInRestaurant = priceList.keySet();
        
         String [] listOfItemsInRestaurant = (String[]) itemsInRestaurant.toArray(new String[0]) ;  
         
         List<List<String>> allCombinations = getMenuItemsCombination(listOfItemsInRestaurant);
         
         Double cost = findComboWithMinPriceForRestaurant(allCombinations, restaurantId);
       
        
        return cost;
        
    }

    private Double findComboWithMinPriceForRestaurant(List<List<String>> allCombinations, Integer restaurantId) {
        Double minPrice = Double.MAX_VALUE;
        Double priceOfCombo = 0.0;
        Map<String,Double> priceList  =  combinedMenu.get(restaurantId);
        for(List<String> combiItem:allCombinations){
            priceOfCombo = 0.0;
            for(String item:combiItem){
                priceOfCombo = priceOfCombo +  priceList.get(item);
            }
            if(priceOfCombo < minPrice){
                minPrice = priceOfCombo;
            }
        }
        return minPrice;
    }

    private List<List<String>> getMenuItemsCombination(String[] listOfItemsInRestaurant) {
        List<List<String>> combosForUserOrder = new ArrayList<List<String>>();
        String number  = StringUtils.EMPTY;
        int length = listOfItemsInRestaurant.length;
       for(int  i=1; i<Math.pow(2, length);i++){
          number = getBinaryRepresentation(i, length);
       
        String comboItem = getCombinationString(listOfItemsInRestaurant, number );
        String [] comboItemArray = comboItem.split(",");
        comboItem = comboItem.replaceAll(",", "-");
        List<String> comboMenuList = new ArrayList<String>(Arrays.asList(comboItemArray)); 
        
        String [] listOfItems = comboItem.split("-");
        List<String> listOfItemsInCombo = new ArrayList<String>(Arrays.asList(listOfItems)); 
        
        if(checkComboForRequest(listOfItemsInCombo)){
            combosForUserOrder.add(comboMenuList);
        }
       }
        return combosForUserOrder;
    }

    private String getBinaryRepresentation(int i, int length) {
        String number  = Integer.toBinaryString(i);
        Integer appendZeros=0;
        if(number.length()<length){
             appendZeros = length - number.length();
        }
        for(int num=0;num<appendZeros;num++){
            number ="0"+number;
            
        }
        return number;
    }

    private Boolean checkComboForRequest( List<String> comboList ) {
        String [] searchItemArray = this.searchItem.trim().split(",");
            List<String> userOrderedItems = new ArrayList<String>(Arrays.asList(searchItemArray));
            if(comboList.containsAll(userOrderedItems)){
                return Boolean.TRUE;
            }else
            {
                return Boolean.FALSE;
            }
    }

    

    private String  getCombinationString(String[] itm, String number) {
        String combiMenu =StringUtils.EMPTY;
        for(int index =0;index < itm.length; index++){
            if(number.charAt(index)=='1'){
                combiMenu = combiMenu + itm[index]+",";
            }
        }
        combiMenu = combiMenu.substring(0,combiMenu.length()-1);
        return combiMenu;
    }

}
