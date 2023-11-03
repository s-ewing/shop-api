package com.example.shopapi.runners;

import com.example.shopapi.enums.ProductCategory;
import com.example.shopapi.models.Product;
import com.example.shopapi.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class ProductSeedRunner implements CommandLineRunner {
    private final ProductRepository productRepository;

    public ProductSeedRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void run(String... args) {
        Product product1 = new Product();
        product1.setName("Grey Brim Hat");
        product1.setDescription("A grey brim hat");
        product1.setPrice(new BigDecimal(45));
        product1.setImgSrc("/images/grey-brim-hat.jpg");
        product1.setCategories(Arrays.asList(ProductCategory.HATS, ProductCategory.MENS));

        Product product2 = new Product();
        product2.setName("Blue Beanie");
        product2.setDescription("A blue beanie");
        product2.setPrice(new BigDecimal(20));
        product2.setImgSrc("/images/blue-beanie.jpg");
        product2.setCategories(Arrays.asList(ProductCategory.HATS, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product3 = new Product();
        product3.setName("Grey Cap");
        product3.setDescription("A grey cap");
        product3.setPrice(new BigDecimal(25));
        product3.setImgSrc("/images/grey-cap.jpg");
        product3.setCategories(Arrays.asList(ProductCategory.HATS, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product4 = new Product();
        product4.setName("Brown Brim Hat");
        product4.setDescription("A brown brim hat");
        product4.setPrice(new BigDecimal(45));
        product4.setImgSrc("/images/brown-brim-hat.jpg");
        product4.setCategories(Arrays.asList(ProductCategory.HATS, ProductCategory.WOMENS));

        Product product5 = new Product();
        product5.setName("Red Puff Jacket");
        product5.setDescription("A red puff jacket");
        product5.setPrice(new BigDecimal(125));
        product5.setImgSrc("/images/red-puff-jacket.jpg");
        product5.setCategories(Arrays.asList(ProductCategory.JACKETS, ProductCategory.MENS));

        Product product6 = new Product();
        product6.setName("Black Denim Jacket");
        product6.setDescription("A black denim jacket");
        product6.setPrice(new BigDecimal(60));
        product6.setImgSrc("/images/black-denim-jacket.jpg");
        product6.setCategories(Arrays.asList(ProductCategory.JACKETS, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product7 = new Product();
        product7.setName("Light Denim Jacket");
        product7.setDescription("A light denim jacket");
        product7.setPrice(new BigDecimal(60));
        product7.setImgSrc("/images/light-denim-jacket.jpg");
        product7.setCategories(Arrays.asList(ProductCategory.JACKETS, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product8 = new Product();
        product8.setName("Notched Lapel Jacket");
        product8.setDescription("A notched lapel jacket");
        product8.setPrice(new BigDecimal(125));
        product8.setImgSrc("/images/notched-lapel-jacket.jpg");
        product8.setCategories(Arrays.asList(ProductCategory.JACKETS, ProductCategory.WOMENS));

        Product product9 = new Product();
        product9.setName("High Top Shoes");
        product9.setDescription("A pair of high top shoes");
        product9.setPrice(new BigDecimal(65));
        product9.setImgSrc("/images/high-top-shoes.jpg");
        product9.setCategories(Arrays.asList(ProductCategory.SHOES, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product10 = new Product();
        product10.setName("White Sneakers");
        product10.setDescription("A pair of white sneakers");
        product10.setPrice(new BigDecimal(80));
        product10.setImgSrc("/images/white-sneakers.jpg");
        product10.setCategories(Arrays.asList(ProductCategory.SHOES, ProductCategory.MENS));

        Product product11 = new Product();
        product11.setName("Leather Boots");
        product11.setDescription("A pair of leather boots");
        product11.setPrice(new BigDecimal(140));
        product11.setImgSrc("/images/leather-boots.jpg");
        product11.setCategories(Arrays.asList(ProductCategory.SHOES, ProductCategory.WOMENS));

        Product product12 = new Product();
        product12.setName("Card Holder");
        product12.setDescription("A card holder");
        product12.setPrice(new BigDecimal(30));
        product12.setImgSrc("/images/card-holder.jpg");
        product12.setCategories(Arrays.asList(ProductCategory.ACCESSORIES, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product13 = new Product();
        product13.setName("Sunglasses");
        product13.setDescription("A pair of sunglasses");
        product13.setPrice(new BigDecimal(75));
        product13.setImgSrc("/images/sunglasses.jpg");
        product13.setCategories(Arrays.asList(ProductCategory.ACCESSORIES, ProductCategory.MENS, ProductCategory.WOMENS));

        Product product14 = new Product();
        product14.setName("Analog Watch");
        product14.setDescription("An analog watch");
        product14.setPrice(new BigDecimal(55));
        product14.setImgSrc("/images/analog-watch.jpg");
        product14.setCategories(Arrays.asList(ProductCategory.ACCESSORIES, ProductCategory.MENS, ProductCategory.WOMENS));

        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10, product11, product12, product13, product14));
    }
}
