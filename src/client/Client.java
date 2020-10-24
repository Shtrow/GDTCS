/*
 * Main class
 */
package client;
import java.io.IOException;

public class Client {
	public static void main(String[] args) throws IOException {
		String userName = "Maitre Simonard";

		client.GDTService GDTService = new GDTService();
		GDTService.run();

		DataProvider dataProvider = new DataProvider(GDTService);

    	Controller c = new Controller(dataProvider);

    	c.run();

		System.out.println("Running client [OK]");
	}
}
