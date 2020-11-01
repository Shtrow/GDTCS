package client;

import common.Logs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

/**
 * Class to display product well
 *
 * @author Marais-Viau
 */
public class ProductViewer {
	private ProductViewer() {
	}

	/**
	 * Display the product list
	 *
	 * @param annonces the lsit of announces
	 */
	public static void displayProducts(String[] annonces) {
		if (annonces == null) {
			System.out.println("NONE");
			return;
		}
		Logs.log("ENTER DISPLAY annonces size : " + annonces.length);
		String[] labels = new String[] { "Product ID", "Domain", "Title", "Description", "Price" };
		ArrayList<String[]> m_annonces = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();
		ArrayList<String> domains = new ArrayList<>();
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<String> descriptions = new ArrayList<>();
		ArrayList<String> prices = new ArrayList<>();
		ids.add(labels[0]);
		domains.add(labels[1]);
		titles.add(labels[2]);
		descriptions.add(labels[3]);
		prices.add(labels[4]);
		m_annonces.add(labels);
		for (int i = 0; i < annonces.length; i += 5) {
			ids.add(annonces[i]);
			domains.add(annonces[i + 1]);
			titles.add(annonces[i + 2]);
			descriptions.add(annonces[i + 3]);
			prices.add(annonces[i + 4]);
			m_annonces.add(Arrays.copyOfRange(annonces, i, i + 5));
		}
		int largerId = largerOf(ids);
		int largerDomain = largerOf(domains);
		int largerTitle = largerOf(titles);
		int largerDescription = largerOf(descriptions);
		int largerPrice = largerOf(prices);

		Runnable printGates = () -> {
			printGate(largerId);
			printGate(largerDomain);
			printGate(largerTitle);
			printGate(largerDescription);
			printGate(largerPrice);
			System.out.print("+");
		};
		m_annonces.forEach(strings -> {
			printGates.run();
			System.out
					.printf(
							"\n|%" + -largerId + "s" + "|%" + -largerDomain + "s" + "|%" + -largerTitle + "s" + "|%"
									+ -largerDescription + "s" + "|%" + -largerPrice + "s|\n",
							strings[0], strings[1], strings[2], strings[3], strings[4]);
			if (strings[0].contains("Product")) {
				printGates.run();
				System.out.println();
			}
			;
		});
		printGates.run();
		System.out.println();

	}

	/**
	 * Print a dthe list of domains
	 *
	 * @param domains the list of domains
	 */
	public static void printDomains(String[] domains) {
		String final_s = "";
		int gateSize = Arrays.stream(domains).mapToInt(String::length).sum() + domains.length - 1;
		printGate(gateSize);
		System.out.print("+");
		for (String s : domains) {
			final_s += "|" + s;
		}
		System.out.println("\n" + final_s + "|");
		printGate(gateSize);
		System.out.println("+");

	}

	private static void printGate(int size) {
		String gate = "";
		for (int i = 0; i < size; i++) {
			gate += '-';
		}
		System.out.print("+" + gate);
	}

	private static int largerOf(ArrayList<String> args) {
		OptionalInt m = args.stream().mapToInt(String::length).max();
		return m.getAsInt();
	}

}
