package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
  private static final String FILE_PATH = "products.txt";
  private ObjectMapper mapper = new ObjectMapper();

  @PostMapping("/products")
  public ResponseEntity<ProductModel> createProduct(@RequestBody ProductModel product) throws IOException {
    addProductToFile(product);
    return ResponseEntity.ok(product);
  }

  @GetMapping("/products")
  public ResponseEntity<List<ProductModel>> getProducts() throws IOException {
    return ResponseEntity.ok(readProductsFromFile());
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<ProductModel> getProductFromId(@PathVariable String id) throws IOException {
    List<ProductModel> productsList = readProductsFromFile();
    ProductModel findProduct = productsList.stream().filter(productModel -> productModel.getId() == Long.parseLong(id)).findFirst().orElse(null);
    if (findProduct == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(findProduct);
  }



  public List<ProductModel> readProductsFromFile() throws IOException {
    File file = new File(FILE_PATH);
    if (!file.exists()){
      return new ArrayList<>();
    }
    return mapper.readValue(file, new TypeReference<List<ProductModel>>(){});
  }

  public void addProductToFile(ProductModel product) throws IOException {
    List<ProductModel> products = readProductsFromFile();
    products.add(product);
    mapper.writeValue(new File(FILE_PATH), products);
  }
}

