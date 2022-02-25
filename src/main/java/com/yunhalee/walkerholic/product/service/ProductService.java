package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
import com.yunhalee.walkerholic.user.dto.UserSellerDTO;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {


    public static final int PRODUCT_PER_PAGE = 9;
    public static final int PRODUCT_LIST_PER_PAGE = 10;

    private ProductRepository productRepository;

    private UserService userService;

    private ProductImageRepository productImageRepository;

    private S3ImageUploader s3ImageUploader;

    private String bucketUrl;

    public ProductService(ProductRepository productRepository, UserService userService,
        ProductImageRepository productImageRepository,
        S3ImageUploader s3ImageUploader,
        @Value("${AWS_S3_BUCKET_URL}") String bucketUrl) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.productImageRepository = productImageRepository;
        this.s3ImageUploader = s3ImageUploader;
        this.bucketUrl = bucketUrl;
    }

    public SimpleProductResponse createProduct(ProductRequest request,
        List<MultipartFile> multipartFiles) {
        User user = userService.findUserById(request.getUserId());
        Product product = productRepository.save(request.toProduct(user));
        saveProductImage(product, multipartFiles);
        return SimpleProductResponse.of(product);
    }

    public SimpleProductResponse updateProduct(ProductRequest request,
        List<MultipartFile> multipartFiles, List<String> deletedImages) {
        Product product = findProductById(request.getId());
        product.update(request.toProduct());
        deleteProductImage(product, deletedImages);
        saveProductImage(product, multipartFiles);
        return SimpleProductResponse.of(product);
    }


    private void saveProductImage(Product product, List<MultipartFile> multipartFiles) {
        Optional.ofNullable(multipartFiles).orElseGet(Collections::emptyList)
            .forEach(multipartFile -> {
                try {
                    String uploadDir = "productUploads/" + product.getId();
                    String imageUrl = s3ImageUploader.uploadFile(uploadDir, multipartFile);
                    String fileName = imageUrl.substring(bucketUrl.length() + uploadDir.length() + 2);
                    ProductImage productImage = productImageRepository.save(ProductImage.of(fileName, imageUrl, product));
                    product.addProductImage(productImage);
                } catch (IOException ex) {
                    new IOException("Could not save file : " + multipartFile.getOriginalFilename());
                }
            });

    }

    private void deleteProductImage(Product product, List<String> deletedImages) {
        Optional.ofNullable(deletedImages).orElseGet(Collections::emptyList)
            .forEach(deletedImage -> {
                product.deleteProductImage(deletedImages);
                productImageRepository.deleteByFilePath(deletedImage);
                String fileName = deletedImage.substring(bucketUrl.length() + 1);
                s3ImageUploader.deleteFile(fileName);
            });
    }

//
//    public ProductListDTO saveProduct(ProductRequest productCreateDTO, List<MultipartFile> multipartFiles, List<String> deletedImages){
//
//        if(productCreateDTO.getId()!=null){
//            Product existingProduct = productRepository.findById(productCreateDTO.getId()).get();
//            existingProduct.setName(productCreateDTO.getName());
//            existingProduct.setDescription(productCreateDTO.getDescription());
//            existingProduct.setBrand(productCreateDTO.getBrand());
//            existingProduct.setCategory(Category.valueOf(productCreateDTO.getCategory()));
//            existingProduct.setStock(productCreateDTO.getStock());
//            existingProduct.setPrice(productCreateDTO.getPrice());
//
//            if(multipartFiles!=null && multipartFiles.size()!=0){
//                saveProductImage(existingProduct,multipartFiles);
//            }
//            if(deletedImages!=null && deletedImages.size()!=0){
//                deleteProductImage(deletedImages);
//            }
//
//            productRepository.save(existingProduct);
//
//            return new ProductListDTO(existingProduct);
//        }else{
//            Product product = new Product();
//            User user = userService.findUserById(productCreateDTO.getUserId());
//            product.setName(productCreateDTO.getName());
//            product.setDescription(productCreateDTO.getDescription());
//            product.setBrand(productCreateDTO.getBrand());
//            product.setCategory(Category.valueOf(productCreateDTO.getCategory()));
//            product.setStock(productCreateDTO.getStock());
//            product.setPrice(productCreateDTO.getPrice());
//            product.setUser(user);
//
//            productRepository.save(product);
//
//            if (multipartFiles.size() != 0) {
//                saveProductImage(product,multipartFiles);
//            }
//
//            productRepository.save(product);
//
//            return new ProductListDTO(product);
//        }
//
//    }

    public ProductResponse getProduct(Integer id) {
        Product product = productRepository.findByProductId(id);
        return new ProductResponse();
    }

    public HashMap<String, Object> getProducts(Integer page, String sort, String category,
        String keyword) {

        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("createdAt"));
        System.out.println(sort);
        if (sort.equals("highest")) {
            pageable = PageRequest
                .of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "price"));
        } else if (sort.equals("lowest")) {
            pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("price"));
        } else if (sort.equals("toprated")) {
            pageable = PageRequest
                .of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "average"));
        }

        List<SimpleProductResponse> productDTOS = new ArrayList<>();
        HashMap<String, Object> productInfo = new HashMap<>();

        Page<Product> productPage;
        if (category == null || category.isBlank()) {
            productPage = productRepository.findAllByKeyword(pageable, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new SimpleProductResponse(product)));
        } else {
            Category category1 = Category.valueOf(category);
            productPage = productRepository.findAllByCategory(pageable, category1, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new SimpleProductResponse(product)));
        }
        productInfo.put("products", productDTOS);
        productInfo.put("totalElement", productPage.getTotalElements());
        productInfo.put("totalPage", productPage.getTotalPages());
        return productInfo;
    }


    public HashMap<String, Object> getProductsBySeller(Integer id, Integer page, String sort,
        String category, String keyword) {

        Pageable pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("createdAt"));

        if (sort.equals("highest")) {
            pageable = PageRequest
                .of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "price"));
        } else if (sort.equals("lowest")) {
            pageable = PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("price"));
        } else if (sort.equals("toprated")) {
            pageable = PageRequest
                .of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "average"));
        }

        List<SimpleProductResponse> productDTOS = new ArrayList<>();
        HashMap<String, Object> productInfo = new HashMap<>();

        Page<Product> productPage;
        if (category == null || category.isBlank()) {
            productPage = productRepository.findAllBySellerAndKeyword(pageable, id, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new SimpleProductResponse(product)));
        } else {
            Category category1 = Category.valueOf(category);
            productPage = productRepository
                .findAllBySellerAndCategory(pageable, id, category1, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new SimpleProductResponse(product)));
        }

        User user = userService.findUserById(id);
        productInfo.put("seller", new UserSellerDTO(user));
        productInfo.put("products", productDTOS);
        productInfo.put("totalElement", productPage.getTotalElements());
        productInfo.put("totalPage", productPage.getTotalPages());
        return productInfo;
    }

    public Integer deleteProduct(Integer id) {
        Product product = productRepository.findByProductId(id);
        String dir = "/productUploads/" + id;
        FileUploadUtils.deleteDir(dir);

        for (ProductImage productImage : product.getProductImages()) {
            productImageRepository.deleteById(productImage.getId());
        }

        productRepository.deleteById(id);
        return id;
    }

    public HashMap<String, Object> getAllProductList(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findAllProductList(pageable);
        List<Product> products = productPage.getContent();
        List<SimpleProductResponse> productListDTOS = new ArrayList<>();

        products.forEach(product -> productListDTOS.add(new SimpleProductResponse(product)));

        HashMap<String, Object> productList = new HashMap<>();
        productList.put("products", productListDTOS);
        productList.put("totalElement", productPage.getTotalElements());
        productList.put("totalPage", productPage.getTotalPages());

        return productList;
    }

    public HashMap<String, Object> getProductListBySeller(Integer page, String sort, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findByUserId(id, pageable);
        List<Product> products = productPage.getContent();
        List<SimpleProductResponse> productListDTOS = new ArrayList<>();

        products.forEach(product -> productListDTOS.add(new SimpleProductResponse(product)));

        HashMap<String, Object> productList = new HashMap<>();
        productList.put("products", productListDTOS);
        productList.put("totalElement", productPage.getTotalElements());
        productList.put("totalPage", productPage.getTotalPages());

        return productList;
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
    }

}
