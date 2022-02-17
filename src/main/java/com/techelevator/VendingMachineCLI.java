package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendingMachineCLI {

	public VendingMachineCLI() {

	}

	public static void main(String[] args) {
		VendingMachineCLI cli = new VendingMachineCLI();
		cli.run();
	}

	public void run() {
		boolean runningStatus = true;
		Machine machine = new Machine();
// Read VendingMachine.txt. Split, set ID's for each line part
		List<Product> productList = new ArrayList<>();
		createProductList(productList);

// Create Map with slotID as key and remaining descriptors as values
		Map<String, Product> productMap = new HashMap<>();
		createProductMap(productList, productMap);

///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
/////////////////// RUN PROGRAM START HERE/////////////////////////

		displayWelcomeMessage();
		while (runningStatus) {
			displayMainMenu();
			System.out.print("Please select number from Main Menu: ");
			Scanner userInput = new Scanner(System.in);
			String mainMenuSelection = userInput.nextLine();

//Main menu 1-4
//Main menu 1: Display Items
			switch (mainMenuSelection) {
				case "1":
					displayItems(productList);
					System.out.println();
					break;

//Main menu 2: Purchase Items
				case "2":
					boolean transaction = true;
					while (transaction) {
						displayPurchaseSubmenu();
						System.out.print("Please select number from Purchase Menu: ");
						String purchaseMenuSelection = userInput.nextLine();

//Submenu 1: Feed Money
						switch (purchaseMenuSelection) {
							case "1": {
								System.out.print("Please insert either $1, $5, or $20 dollar bills here: ");
								String moneyFed = userInput.nextLine();
								if ((!moneyFed.equals("1")) && !moneyFed.equals("5") && !moneyFed.equals("20")) {
									System.out.println("\n" + "This is not a valid amount. Please try again.");
									System.out.println("Your current balance is $" + machine.getBalance() + "\n");
								break;
								}
								int moneyFedInt = Integer.parseInt(moneyFed);
								BigDecimal moneyFedBD = BigDecimal.valueOf(moneyFedInt).setScale(2, RoundingMode.HALF_UP);
								machine.setInsertMoney(moneyFedInt);
								machine.increaseBalance();

//**** Print money fed to Log.txt
								File outputFile = new File("src/main/resources/log.txt");
								try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFile, true))) {
									printWriter.write(auditFeed(moneyFedBD, machine.getBalance(), "FEED MONEY: "));
								} catch (FileNotFoundException e) {
									System.out.println("File Not Found");
								}
								System.out.println("\n" + "You inserted $" + moneyFedBD);
								System.out.println("Current balance is $" + machine.getBalance() + "\n");
								break;
							}

//Submenu 2: Select Product
							case "2":
								displayItems(productList);
								System.out.print("\n" + "Please enter item slot ID: ");
								String itemSelection = userInput.nextLine().trim().toUpperCase();
								if (productMap.containsKey(itemSelection)) {

									// Didn't enter money
									if (machine.getBalance().compareTo(new BigDecimal("0.00")) == 0) {
										System.out.println("\n" + "Please deposit money before selecting items." + "\n");

									// Not enough money or sold out...
									} else if (productMap.get(itemSelection).getPrice().compareTo(machine.getBalance()) > 0 || productMap.get(itemSelection).getInventory() == 0) {
										System.out.println("\n" + "Oops, seems you don't have enough money or item is sold out! Please try again." + "\n");

									// User selects improper choice
									} else if (productMap.get(itemSelection) == null) {
										System.out.println("\n" + "Oops, this is not a valid item selection. Please select from existing items." + "\n");

									// Let's buy something!
									} else {
										machine.setPrice(productMap.get(itemSelection).getPrice());
										machine.decreaseBalance();
										System.out.println("\n" + "You purchased: " +  productMap.get(itemSelection).getName());
										System.out.println("Current balance is $" + machine.getBalance());
										productMap.get(itemSelection).setInventory(productMap.get(itemSelection).getInventory() - 1);
										System.out.println("\n" + productMap.get(itemSelection).getSound() + "\n");

//***Print purchase to Log.txt
										File outputFile = new File("src/main/resources/log.txt");
										try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFile, true))) {
											printWriter.write(auditFeed(productMap.get(itemSelection).getPrice(), machine.getBalance(), productMap.get(itemSelection).getName()
													+ " " + productMap.get(itemSelection).getSlotID() + " "));
										} catch (FileNotFoundException e) {
											System.out.println("File Not Found");
										}
									}

								// User inputs invalid slotID
								} else {
									System.out.println("\n" + "Please enter valid slotID." + "\n");
								} break;

//Submenu 3: Finish Transaction
							case "3": {
								transaction = false;
								BigDecimal oneHundred = new BigDecimal("100");
								int balanceInCent = machine.getBalance().multiply(oneHundred).intValue();
								int quarter = balanceInCent / 25;
								int dime = balanceInCent % 25 / 10;
								int nickel = balanceInCent % 25 % 10 / 5;
								System.out.println("\n" + "Your change is " + quarter + " quarter(s)," +
										" " + dime + " dime(s), and " + nickel + " nickel(s).");
								BigDecimal change = machine.getBalance();
								machine.setBalance(new BigDecimal("0"));

//**** Print change given to Log.txt
								File outputFile = new File("src/main/resources/log.txt");
								try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(outputFile, true))) {
									printWriter.write(auditFeed(change, machine.getBalance().setScale(2, RoundingMode.HALF_UP), "GIVE CHANGE "));
								} catch (FileNotFoundException e) {
									System.out.println("File Not Found");
								} break;
							}

							// User does not input 1, 2, or 3.
							default:
								System.out.println("Selection is invalid. Please select valid selection " +
										"from purchase menu." + "\n");
							break;
						}
					}
					break;

//Main Menu 3: Exit
				case "3":
					System.out.println("Thanks for you purchase. Enjoy your snacks!" + "\n");
					displayWelcomeMessage();
					break;

//Main Menu 4: Hidden Sales Report. Display to owner.
				case "4":
					BigDecimal totalSales = new BigDecimal("0.00");
					for (Product saleInformation : productList) {
						System.out.println(saleInformation.getName() + "|" + (5 - saleInformation.getInventory()));
						BigDecimal salesNumber = new BigDecimal(5 - saleInformation.getInventory());
						totalSales = totalSales.add(saleInformation.getPrice().multiply(salesNumber));
					}
					System.out.println();
					System.out.println("**TOTAL SALES** " + totalSales);
				break;

				// User does not input 1, 2, 3, or 4.
				default:
					System.out.println("Selection is invalid. Please select valid selection " +
							"from main menu.");
				break;
			}
		}
	}

	public void displayWelcomeMessage(){
		System.out.println(" __     __            _              __    __       _   _       ");
		System.out.println(" \\ \\   / /__ _ __  __| | ___         |  \\ /  | ____| |_(_) ___  ");
		System.out.println("  \\ \\ / / __\\ '_ \\/ _` |/ _ \\ _______|  |\\/| |/  _`| __| |/ __| ");
		System.out.println("   \\ V /  __/ | | | (_| |(_) |_______|  |  | | (_| | |_| | (__  ");
		System.out.println("    \\_/ \\___|_| |_|\\___|\\___/        |__|  |_|\\___| \\__|_|\\___|");
		System.out.println("                        __    ___   ___                         	  ");
		System.out.println("                       / /_  / _ \\ / _ \\						      ");
		System.out.println("                 _____| '_ \\| | | | | | |_____                       ");
		System.out.println("                |_____| (_) | |_| | |_| |_____|                      ");
		System.out.println("                       \\___/ \\___/ \\___/                             ");
	}

//Main Menu
	public void displayMainMenu() {
		System.out.println();
		System.out.println(" -Main Menu- ");
		System.out.println("1. Display Vending Machine Items");
		System.out.println("2. Purchase Items");
		System.out.println("3. Exit" + "\n");
	}

//Purchase Item Submenu
	public void displayPurchaseSubmenu() {
		System.out.println("  1) Feed Money");
		System.out.println("  2) Select Product");
		System.out.println("  3) Finish Transaction" + "\n");
	}

//Item Display
	public void displayItems(List<Product> productList) {
		for (Product itemDisplayed: productList) {
			if(itemDisplayed.getInventory() < 1) {
				System.out.println("Item is sold out" + "\n");
			} else {
				System.out.println(itemDisplayed.getSlotID() + ": " + itemDisplayed.getName()+ ", " +  itemDisplayed.getPrice());
			}
		}
	}

//Read File and Create Product List
	public void createProductList(List<Product> productList) {
		File inputFile = new File("ExampleFiles/VendingMachine.txt");
		try (Scanner textFile = new Scanner(inputFile)) {
			while (textFile.hasNextLine()) {
				String line = textFile.nextLine();
				String[] lineParts = line.split("\\|");
				BigDecimal priceDecimal = new BigDecimal(lineParts[2]).setScale(2, RoundingMode.HALF_UP);
				Product product = new Product();
				product.setSlotID(lineParts[0]);
				product.setName(lineParts[1]);
				product.setPrice(priceDecimal);
				product.setTypeName(lineParts[3]);
				product.setInventory(5);
				product.getSound();
				productList.add(product);
			}
		} catch (FileNotFoundException e) {
			System.out.println("This file could not be found.");
		}
	}

//Create Product Map
	public void createProductMap(List<Product> productList, Map<String, Product>productMap) {
		for (Product searchProduct : productList) {
			productMap.put(searchProduct.getSlotID(), searchProduct);
		}
	}

//Send Audit Feed to Log.txt
	public String auditFeed(BigDecimal moneyfedBD, BigDecimal balance, String label) {
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		String dateAndTime = dateTime.format(LocalDateTime.now());
		return (dateAndTime + " " + label + "$" + moneyfedBD + " $" + balance + "\n");
	}

}

