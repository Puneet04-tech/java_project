package com.inventory;

import com.inventory.controller.InventoryController;
import com.inventory.util.Logger;

/**
 * Main application entry point for Smart Inventory Management System
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-11-23
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            Logger.info("=".repeat(60));
            Logger.info("Smart Inventory Management System - Starting Application");
            Logger.info("Version: 1.0");
            Logger.info("=".repeat(60));
            
            // Create and start the controller
            InventoryController controller = new InventoryController();
            controller.start();
            
            Logger.info("Application terminated successfully");
            
        } catch (Exception e) {
            System.err.println("Critical error occurred: " + e.getMessage());
            Logger.error("Critical application error", e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
