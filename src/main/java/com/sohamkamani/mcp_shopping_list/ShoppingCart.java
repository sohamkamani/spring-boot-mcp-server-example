package com.sohamkamani.mcp_shopping_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCart {

  public record ShoppingItem(String name, int quantity) {
  }

  private final Map<String, ShoppingItem> shoppingList = new ConcurrentHashMap<>();

  @Tool(name = "addItem",
      description = "Add an item to the shopping list or update its quantity. Specify item name and quantity.")
  public String addItem(String name, int quantity) {
    if (name == null || name.trim().isEmpty() || quantity <= 0) {
      return "Error: Invalid item name or quantity.";
    }
    shoppingList.compute(name.toLowerCase(), (key, existingItem) -> {
      if (existingItem == null) {
        return new ShoppingItem(name, quantity);
      } else {
        return new ShoppingItem(existingItem.name(), existingItem.quantity() + quantity);
      }
    });
    return "Added " + quantity + " of " + name + " to the shopping list.";
  }

  @Tool(name = "getItems",
      description = "Get all items currently in the shopping list. Returns a list of items with their names and quantities.")
  public List<ShoppingItem> getItems() {
    return new ArrayList<>(shoppingList.values());
  }

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
      shoppingList.put(lowerCaseName, new ShoppingItem(item.name(), item.quantity() - quantity));
      return "Removed " + quantity + " of '" + name + "'. Remaining quantity: "
          + shoppingList.get(lowerCaseName).quantity() + ".";
    }
  }
}
