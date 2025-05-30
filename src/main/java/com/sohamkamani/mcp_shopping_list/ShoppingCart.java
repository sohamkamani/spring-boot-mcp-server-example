package com.sohamkamani.mcp_shopping_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service; // Add Service annotation

@Service // Mark this class as a Spring Service
public class ShoppingCart {

  // Define ShoppingItem as a nested record
  public record ShoppingItem(String name, int quantity) {
  }

  // Use a ConcurrentHashMap to store the shopping list items
  private final Map<String, ShoppingItem> shoppingList = new ConcurrentHashMap<>();

  /**
   * Adds an item to the shopping list or updates the quantity if it already exists.
   *
   * @param name The name of the item.
   * @param quantity The quantity to add.
   * @return A confirmation message.
   */
  @Tool(name = "addItem",
      description = "Add an item to the shopping list or update its quantity. Specify item name and quantity.")
  public String addItem(String name, int quantity) {
    if (name == null || name.trim().isEmpty() || quantity <= 0) {
      return "Error: Invalid item name or quantity.";
    }
    shoppingList.compute(name.toLowerCase(), (key, existingItem) -> {
      if (existingItem == null) {
        // Create a new ShoppingItem record
        return new ShoppingItem(name, quantity);
      } else {
        // Create a new ShoppingItem record with updated quantity
        return new ShoppingItem(existingItem.name(), existingItem.quantity() + quantity);
      }
    });
    return "Added " + quantity + " of " + name + " to the shopping list.";
  }

  /**
   * Gets all items currently in the shopping list.
   *
   * @return A list of all shopping items.
   */
  @Tool(name = "getItems",
      description = "Get all items currently in the shopping list. Returns a list of items with their names and quantities.")
  public List<ShoppingItem> getItems() {
    return new ArrayList<>(shoppingList.values());
  }

  /**
   * Removes a specified quantity of an item from the shopping list, or removes the item entirely if
   * the quantity to remove is greater than or equal to the current quantity.
   *
   * @param name The name of the item to remove.
   * @param quantity The quantity to remove.
   * @return A confirmation or error message.
   */
  @Tool(name = "removeItem",
      description = "Remove a specified quantity of an item from the shopping list. Specify item name and quantity to remove. If quantity is not specified or is greater than item quantity, the item is removed.")
  public String removeItem(String name, int quantity) {
    if (name == null || name.trim().isEmpty()) {
      return "Error: Invalid item name.";
    }
    String lowerCaseName = name.toLowerCase();
    ShoppingItem item = shoppingList.get(lowerCaseName);

    if (item == null) {
      return "Error: Item '" + name + "' not found in the shopping list.";
    }

    if (quantity <= 0 || quantity >= item.quantity()) {
      shoppingList.remove(lowerCaseName);
      return "Removed '" + name + "' from the shopping list.";
    } else {
      // Create a new ShoppingItem record with updated quantity
      shoppingList.put(lowerCaseName, new ShoppingItem(item.name(), item.quantity() - quantity));
      return "Removed " + quantity + " of '" + name + "'. Remaining quantity: "
          + shoppingList.get(lowerCaseName).quantity() + ".";
    }
  }
}
