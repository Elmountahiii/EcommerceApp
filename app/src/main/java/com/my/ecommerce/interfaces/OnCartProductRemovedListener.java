package com.my.ecommerce.interfaces;

public interface OnCartProductRemovedListener {

    void onProductRemoved(int productPosition);
    void addToPrice(float priceToBeAdd);
    void minusFromPrice(float priceToBeMinus);
}
