package com.example.demo.Repository;

import com.example.demo.Modal.Products;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends MongoRepository<Products, String> {

    @Query("{ '_id' : ?0 }")
    Products findByProductId(ObjectId id);
    @Query("{ '_id' : ?0 }")
    List<Products> findByProductList(String id);

    @Query("{ 'fileData' : ?0 }")
    byte[] getFileData(String id);
    @Query("{ 'contentType' : ?0 }")
    String getContentType(String id);
    @Query("{ '_id' : ?0 }")
    List<Products> getProductDetails(String id);
    @Query("{ 'title' : ?0 }")
    String getTitle(String query);

    void deleteById(ObjectId id);
    @Query("{ 'category' : { $in: ['clothing'] } }")
    List<Products> findClothsById(ObjectId id);
    @Query("{ 'category' : { $in: ['Mobiles&Accessories'] } }")
    List<Products> findMobileById(ObjectId id);
    @Query("{ 'category' : { $in: ['books'] } }")
    List<Products> findBookById(ObjectId id);
    @Query("{ 'category' : { $in: ['electronics'] } }")
    List<Products> findElectronicById(ObjectId id);
    @Query("{ 'category' : { $in: ['Tv'] } }")
    List<Products> findTvById(ObjectId id);
    @Query("{ 'category' : { $in: ['Laptops&Accessories'] } }")
    List<Products> findLaptopById(ObjectId id);
    @Query("{ 'category' : { $in: ['TV&HomeEntertainment'] } }")
    List<Products> findHomeEntertainementById(ObjectId id);
    @Query("{ 'category' : { $in: ['Audio'] } }")
    List<Products> findAudiosById(ObjectId id);
    @Query("{ 'category' : { $in: ['Cameras'] } }")
    List<Products> findCamerasById(ObjectId id);
    @Query("{ 'category' : { $in: ['ComputerPeripherals'] } }")
    List<Products> findPcPartsById(ObjectId id);
    @Query("{ 'category' : ?0 }")
    Page<Products> findAll(org.springframework.data.mongodb.core.query.Query query, PageRequest pageRequest);
    @Query("{ 'title' : ?0 }")
    Optional<Products> searchProducts(String query);
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Optional<Products> findByTitle(String id);
    @Query("{ 'category' : { $in: ['Fruits&Vegetables'] } }")
    List<Products> findAllFruitsVegetablesList();
    @Query("{ 'category' : { $in: ['Foodgrains&Oil&Masala&Vegetables'] } }")
    List<Products> findAllFoodgrains_Oil_Masala_egetabless();
@Query("{'category':{$in: ['Beverages']}}")
    List<Products> findAllBevarages();
@Query("{'category':{$in: ['Eggs&Meat&Fish'] }}")
    List<Products> findAllEggsMeatFish();
}
