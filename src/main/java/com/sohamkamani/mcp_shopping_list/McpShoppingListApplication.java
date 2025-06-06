package com.sohamkamani.mcp_shopping_list;

import java.util.List;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Needed to expose the tools

@SpringBootApplication
public class McpShoppingListApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpShoppingListApplication.class, args);
	}

	@Bean
	public List<ToolCallback> tools(ShoppingCart shoppingCart) {
		return List.of(ToolCallbacks.from(shoppingCart));
	}

}
