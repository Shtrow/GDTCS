import java.util.Arrays;

public class ProductViewer {
    public ProductViewer() {

    }
    public void displayOneProduct(String[] strings){
      System.out.println("Product :");
       //TODO : Make a nice array in asci
        Arrays.stream(strings).forEach(System.out::println);
    }

    public void displayProductList(String[] strings){
      System.out.println("Product list :");
       //TODO : Make a nice array in asci
        Arrays.stream(strings).forEach(System.out::println);
    }
}
