package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.product.dto.ProductCreateDTO;
import com.yunhalee.walkerholic.product.dto.ProductDTO;
import com.yunhalee.walkerholic.product.dto.ProductListDTO;
import com.yunhalee.walkerholic.user.dto.UserSellerDTO;
import com.yunhalee.walkerholic.util.AmazonS3Utils;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
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
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ProductImageRepository productImageRepository;

    public static final int PRODUCT_PER_PAGE = 9;

    public static final int PRODUCT_LIST_PER_PAGE = 10;

    private final AmazonS3Utils amazonS3Utils;

    @Value("${AWS_S3_BUCKET_URL}")
    private String AWS_S3_BUCKET_URL;

    private void deleteProductImage(List<String> deletedImages){
        for (String deletedImage : deletedImages) {
            productImageRepository.deleteByFilePath(deletedImage);
            String fileName = deletedImage.substring(AWS_S3_BUCKET_URL.length()+1);
            amazonS3Utils.deleteFile(fileName);
        }
    }

    private void saveProductImage(Product product, List<MultipartFile> multipartFiles){
        multipartFiles.forEach(multipartFile -> {
            ProductImage productImage = new ProductImage();
            try{
                String uploadDir = "productUploads/" + product.getId();
                String imageUrl = amazonS3Utils.uploadFile(uploadDir, multipartFile);
                String fileName = imageUrl.substring(AWS_S3_BUCKET_URL.length()+uploadDir.length()+2);
                productImage.setName(fileName);
                productImage.setFilePath(imageUrl);
                productImage.setProduct(product);
                productImageRepository.save(productImage);
                product.addProductImage(productImage);

            }catch (IOException ex){
                new IOException("Could not save file : " + multipartFile.getOriginalFilename());
            }
        });

    }

    public ProductListDTO saveProduct(ProductCreateDTO productCreateDTO, List<MultipartFile> multipartFiles, List<String> deletedImages){

        if(productCreateDTO.getId()!=null){
            Product existingProduct = productRepository.findById(productCreateDTO.getId()).get();
            existingProduct.setName(productCreateDTO.getName());
            existingProduct.setDescription(productCreateDTO.getDescription());
            existingProduct.setBrand(productCreateDTO.getBrand());
            existingProduct.setCategory(Category.valueOf(productCreateDTO.getCategory()));
            existingProduct.setStock(productCreateDTO.getStock());
            existingProduct.setPrice(productCreateDTO.getPrice());

            if(multipartFiles!=null && multipartFiles.size()!=0){
                saveProductImage(existingProduct,multipartFiles);
            }
            if(deletedImages!=null && deletedImages.size()!=0){
                deleteProductImage(deletedImages);
            }

            productRepository.save(existingProduct);

            return new ProductListDTO(existingProduct);
        }else{
            Product product = new Product();
            User user = userRepository.findById(productCreateDTO.getUserId()).get();
            product.setName(productCreateDTO.getName());
            product.setDescription(productCreateDTO.getDescription());
            product.setBrand(productCreateDTO.getBrand());
            product.setCategory(Category.valueOf(productCreateDTO.getCategory()));
            product.setStock(productCreateDTO.getStock());
            product.setPrice(productCreateDTO.getPrice());
            product.setUser(user);

            productRepository.save(product);

            if (multipartFiles.size() != 0) {
                saveProductImage(product,multipartFiles);
            }

            productRepository.save(product);

            return new ProductListDTO(product);
        }

    }

    public ProductDTO getProduct(Integer id){
        Product product = productRepository.findByProductId(id);
        return new ProductDTO(product);
    }

    public HashMap<String,Object> getProducts(Integer page,String sort, String category, String keyword){

        Pageable pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by("createdAt"));
        System.out.println(sort);
        if(sort.equals("highest")){
            pageable = PageRequest.of(page-1,PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC,"price"));
        }else if(sort.equals("lowest")){
            pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by("price"));
        }else if(sort.equals("toprated")){
            pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "average"));
        }

        List<ProductListDTO> productDTOS = new ArrayList<>();
        HashMap<String, Object> productInfo = new HashMap<>();


        Page<Product> productPage;
        if(category == null  ||  category.isBlank()){
            productPage = productRepository.findAllByKeyword(pageable, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new ProductListDTO(product)));
        }else{
            Category category1 = Category.valueOf(category);
            productPage = productRepository.findAllByCategory(pageable, category1, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new ProductListDTO(product)));
        }
        productInfo.put("products",productDTOS);
        productInfo.put("totalElement", productPage.getTotalElements());
        productInfo.put("totalPage", productPage.getTotalPages());
        return productInfo;
    }


    public HashMap<String,Object> getProductsBySeller(Integer id, Integer page,String sort, String category, String keyword){

        Pageable pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by("createdAt"));

        if(sort.equals("highest")){
            pageable = PageRequest.of(page-1,PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC,"price"));
        }else if(sort.equals("lowest")){
            pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by("price"));
        }else if(sort.equals("toprated")){
            pageable = PageRequest.of(page-1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "average"));
        }

        List<ProductListDTO> productDTOS = new ArrayList<>();
        HashMap<String, Object> productInfo = new HashMap<>();

        Page<Product> productPage;
        if(category == null  ||  category.isBlank()){
            productPage = productRepository.findAllBySellerAndKeyword(pageable,id, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new ProductListDTO(product)));
        }else{
            Category category1 = Category.valueOf(category);
            productPage = productRepository.findAllBySellerAndCategory(pageable, id,category1, keyword);
            List<Product> products = productPage.getContent();
            products.forEach(product -> productDTOS.add(new ProductListDTO(product)));
        }

        User user = userRepository.findById(id).get();
        productInfo.put("seller", new UserSellerDTO(user));
        productInfo.put("products",productDTOS);
        productInfo.put("totalElement", productPage.getTotalElements());
        productInfo.put("totalPage", productPage.getTotalPages());
        return productInfo;
    }

    public Integer deleteProduct(Integer id){
        Product product = productRepository.findByProductId(id);
        String dir = "/productUploads/"+ id;
        FileUploadUtils.deleteDir(dir);

        for (ProductImage productImage : product.getProductImages()) {
            productImageRepository.deleteById(productImage.getId());
        }

        productRepository.deleteById(id);
        return id;
    }

    public HashMap<String, Object> getAllProductList(Integer page, String sort){
        Pageable pageable = PageRequest.of(page-1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findAllProductList(pageable);
        List<Product> products = productPage.getContent();
        List<ProductListDTO> productListDTOS = new ArrayList<>();

        products.forEach(product -> productListDTOS.add(new ProductListDTO(product)));

        HashMap<String, Object> productList = new HashMap<>();
        productList.put("products", productListDTOS);
        productList.put("totalElement", productPage.getTotalElements());
        productList.put("totalPage", productPage.getTotalPages());

        return productList;
    }

    public HashMap<String, Object> getProductListBySeller(Integer page, String sort, Integer id){
        Pageable pageable = PageRequest.of(page-1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findByUserId(id,pageable);
        List<Product> products = productPage.getContent();
        List<ProductListDTO> productListDTOS = new ArrayList<>();

        products.forEach(product -> productListDTOS.add(new ProductListDTO(product)));

        HashMap<String, Object> productList = new HashMap<>();
        productList.put("products", productListDTOS);
        productList.put("totalElement", productPage.getTotalElements());
        productList.put("totalPage", productPage.getTotalPages());

        return productList;
    }

}
