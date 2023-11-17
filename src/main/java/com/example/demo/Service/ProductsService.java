package com.example.demo.Service;

import com.example.demo.Modal.Products;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public interface ProductsService {

    BsonValue saveQuantity(Products  products) throws IOException;
    ObjectId saveFormWithImages(MultipartFile[] files, Products products) throws IOException;
    List<Products> getProductDetails();
    Products getProductDetails(String id);
    Products getProductDetails(ObjectId id);
    List<Products> getProduct(ObjectId id);
    byte[] getFileData(String id);
    String getContentType(String id);
    byte[] getFileName(String id);
    void deleteItem(ObjectId productId);
    List<Products> findById(ObjectId productId);


    Products getImageById(ObjectId imageId);

    Products getProductById(String productId);




    Products saveFormWithImages(Products product);

    Products getCartItems(Products cartItem);

    Page<Products> getPaginatedProducts(int page, int pageSize, String category);

    Optional<Products> findByTitle(String query);
}
