package com.techelevator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test VendingMachineCLI Class")
public class TestVendingMachineCLI {

        VendingMachineCLI vmTest = new VendingMachineCLI();
        List<Product> productList = new ArrayList<>();
        Map<String, Product> productMap = new HashMap<>();

 //Assert
    @Test
    @DisplayName("Product List Displays Correctly")
    public void test_ProductList_Print() {
        vmTest.createProductList(productList);
        assertEquals("A1", productList.get(0).getSlotID(),
                "Product list is not displaying correctly."  );
    }

    @Test
    @DisplayName("Product Map Displays Correctly")
    public void test_ProductMap(){
        vmTest.createProductList(productList);
        vmTest.createProductMap(productList, productMap);
        assertEquals("Potato Crisps", (productMap.get(productList.get(0).getSlotID()).getName()),
                "Map is not displaying correctly");
    }

//    @Test
//    @DisplayName("Feed Money Only Allows 1, 5, and 20")
//    public void test_FeedMoney_Rules() {
//        vmTest.feedMoneyMenu();
//        assertEquals("5", )
//    }



}






