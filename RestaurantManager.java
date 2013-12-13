/*
 *  Any use of the Material is governed by the terms of the actual license
 *  agreement between LeanTaaS Inc. and the user.
 *  Copyright 2010 LeanTaaS Inc., LOS GATOS CA USA.
 *  All rights reserved. Any rights not expressly granted herein are
 *  reserved.
 */
package com.test;

import java.io.File;
import java.io.IOException;

public class RestaurantManager {
    
    static RestaurantService restaurantService = new RestaurantService();

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
         restaurantService.processAndStore(fileName,items);
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
    



   

    
}
