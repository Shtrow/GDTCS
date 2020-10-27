
package client;

import common.Annonce;

import java.util.Arrays;

public class ProductViewer {
    public ProductViewer() {

    }
    public void displayOneProduct(Annonce annonce){
      System.out.println("Product :");
       //TODO : Make a nice array in asci
        Arrays.stream(annonce.toStringArgs()).forEach(System.out::println);
    }

    public void displayProductList(String[] strings){
      System.out.println("Product list :");
       //TODO : Make a nice array in asci
        Arrays.stream(strings).forEach(System.out::println);
    }
}
