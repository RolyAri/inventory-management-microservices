package com.rolydev.inventory_service.services;

import com.rolydev.inventory_service.model.dtos.BaseResponse;
import com.rolydev.inventory_service.model.dtos.OrderItemsRequest;
import com.rolydev.inventory_service.model.entities.Inventory;
import com.rolydev.inventory_service.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySku(sku);
        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    public BaseResponse areInStock(List<OrderItemsRequest> orderItems) {
        var errorList = new ArrayList<String>();
        List<String> skus = orderItems.stream().map(OrderItemsRequest::getSku).toList();
        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach(orderItemsRequest -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItemsRequest.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errorList.add("Product with sku " + orderItemsRequest.getSku() + " does not exist");
            } else if (inventory.get().getQuantity() < orderItemsRequest.getQuantity()) {
                errorList.add("Product with sku " + orderItemsRequest.getSku() + " has insufficient quantity");
            }
        });
        return errorList.size() > 0 ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);

    }
}
