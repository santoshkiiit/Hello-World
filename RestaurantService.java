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


    
    public void searchForEconomicalRestaurant(Map<Integer, Map<String, Double>> combinedMenu, String searchItem) {
        this.combinedMenu =combinedMenu;
        this.searchItem=searchItem;
        //calculates mininum price for user requested items across all the restaurants
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
        //returns the combo with least cost and whic covers all the items in a particular restaurant
        Map<String,Double> priceList  =  combinedMenu.get(restaurantId);
       
        Set<String> itemsInRestaurant = priceList.keySet();
        
         String [] listOfItemsInRestaurant = (String[]) itemsInRestaurant.toArray(new String[0]) ;  
         
         List<List<String>> allCombinations = getMenuItemsCombination(listOfItemsInRestaurant);
         
         Double cost = findComboWithMinPriceForRestaurant(allCombinations, restaurantId);
       
        
        return cost;
        
    }

    private Double findComboWithMinPriceForRestaurant(List<List<String>> allCombinations, Integer restaurantId) {
        //compares cost of all combos in a restaurant and returns combo with least price
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
        //returns all the possible combinations of items/combi in restaurant which cover the uers requested itemd
        List<List<String>> combosForUserOrder = new ArrayList<List<String>>();
        String number  = StringUtils.EMPTY;
        int length = listOfItemsInRestaurant.length;
       for(int  index=1; index<Math.pow(2, length);index++){
          number = getBinaryRepresentation(index, length);
       
        String comboItem = getCombinationString(listOfItemsInRestaurant, number );
        String [] comboItemArray = comboItem.split(",");
        // split the string to get array of items/combis present in restaurant
        comboItem = comboItem.replaceAll(",", "-");
        // split to get list of all items covered in the combi
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
        //returns binary representation of integer
        String combination  = Integer.toBinaryString(i);
        Integer prependZeros=0;
        if(combination.length()<length){
            
             prependZeros = length - combination.length();
        }
        for(int num=0;num<prependZeros;num++){
            combination ="0"+combination;
            
        }
        return combination;
    }

    private Boolean checkComboForRequest( List<String> comboList ) {
        //check if combination items has all the items user requested for
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
        //returns a unique combination of menu items in restaurant using binary representation
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
