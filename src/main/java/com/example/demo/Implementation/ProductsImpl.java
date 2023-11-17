package com.example.demo.Implementation;

import com.example.demo.Modal.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductsImpl implements ProductsService {
    private final ProductsRepository productsRepository; // You would inject your repository here
    private final MongoTemplate mongoTemplate; // You would inject an image service here

    @Autowired
    public ProductsImpl(ProductsRepository productsRepository, MongoTemplate mongoTemplate) {
        this.productsRepository = productsRepository;
        this.mongoTemplate = mongoTemplate;
    }
@Autowired
private ProductsService productsService;



    @Override
    public BsonValue saveQuantity(Products products) throws IOException {
        Query query = new Query(Criteria.where("_id").is(products.getId()));

        Update update = new Update();
        update.set("quantity", products.getQuantity());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Products.class);
        return result.getUpsertedId(); // Return the ID of the updated document
    }
    @Override
    public ObjectId saveFormWithImages(MultipartFile[] files, Products products) throws IOException {

        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setFileData(file.getBytes());
            images.add(image);
        }
Products product=new Products();

        Products items= product.getProduct();
        products.setImages(images); // Set the list of images in the Products object

        // Set other fields from the form data
        products.setPrice(products.getPrice());
        products.setDescription(products.getDescription());
        products.setTitle(products.getTitle());
        products.setCategory(products.getCategory());
        products.setOrderDate(products.getOrderDate());
        products.setQuantity(products.getQuantity());
        products.setProduct(items);
        // Insert the Products object into the products collection
        Products savedProducts = productsRepository.save(products);

        return savedProducts.getId();
    }
    @Override
    public List<Products> getProductDetails() {
        Query query = new Query(); // Empty query to retrieve all products
        List<Products> products = mongoTemplate.find(query, Products.class);
        return products;
    }


    @Override
    public Products getProductDetails(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Products product = mongoTemplate.findOne(query, Products.class);
        return product;
    }
    @Override
    public Products getProductDetails(ObjectId id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Products product = mongoTemplate.findOne(query, Products.class);
        return product;
    }

    @Override
    public List<Products> getProduct(ObjectId id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        List<Products>  product = Collections.singletonList(mongoTemplate.findOne(query, Products.class));
        return product;
    }


    @Override
    public String getContentType(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Document candidateDocument = mongoTemplate.findOne(query, Document.class, "products");

        if (candidateDocument != null) {
            return candidateDocument.getString("contentType");
        }

        return null;
    }


    @Override
    public byte[] getFileName(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Document product = mongoTemplate.findOne(query, Document.class, "products");

        if (product != null) {
            return product.getString("fileName").getBytes();
        }

        return null;
    }

    @Override
    public byte[] getFileData(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Document candidateDocument = mongoTemplate.findOne(query, Document.class, "products");

        if (candidateDocument != null) {
            Binary fileData = candidateDocument.get("fileData", Binary.class);
            if (fileData != null) {
                return fileData.getData();
            }
        }

        return null;
    }

    @Override
    public void deleteItem(ObjectId productId) {
        Query query = Query.query(Criteria.where("_id").is(productId));
        mongoTemplate.remove(query, "products");
    }




    @Override
    public List<Products> findById(ObjectId id) {
        Products product = productsRepository.findById(id.toString()).orElse(null);
        return Collections.singletonList(product);
    }

    @Override
    public Products getImageById(ObjectId imageId) {
        return productsRepository.findById(String.valueOf(imageId)).orElse(null);
    }

    @Override
    public Products getProductById(String productId) {
        Optional<Products> optionalProduct = productsRepository.findById(productId);
        return optionalProduct.orElse(null);
    }


    @Override
    public Products saveFormWithImages(Products products) {
        Products items = products.getProduct();

        // Set fields from the form data and the items object
        products.setPrice(items.getPrice());
        products.setDescription(items.getDescription());
        products.setTitle(items.getTitle());
        products.setCategory(items.getCategory());
        products.setOrderDate(items.getOrderDate());
        products.setQuantity(items.getQuantity());

        // Insert the Products object into the products collection
        Products savedProducts = productsRepository.save(products);

        return savedProducts;
    }

    @Override
    public Products getCartItems(Products cartItem) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(cartItem.getTitle())
                .and("brand").is(cartItem.getBrand())
                .and("category").is(cartItem.getCategory()));
        Products retrievedCartItem = mongoTemplate.findOne(query, Products.class);
        return retrievedCartItem;
    }

    @Override
    public Page<Products> getPaginatedProducts(int page, int pageSize, String category) {
        // Create a PageRequest to specify the page number and page size
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize); // Page number is 0-based

        // Create a query to filter products by category (if provided)
        // You can customize this query based on your data model
        Query query = new Query();
        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("category").is(category));
        }

        // Use the PageRequest and Query to fetch paginated data
        Page<Products> products = productsRepository.findAll(query, pageRequest);

        return products;
    }

    @Override
    public Optional<Products> findByTitle(String query) {
        // Assuming "title" is the property in your "Products" entity to search by
        return productsRepository.findByTitle(query);
    }


}


