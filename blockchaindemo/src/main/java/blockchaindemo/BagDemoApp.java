package blockchaindemo;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class BagDemoApp {

	public static void main(String[] args) {

		BagDemoApp demoApp = new BagDemoApp();
		demoApp.demo3();

	}

	public void demo3() {

		String myBagTag = randomBagTagID();
		String myPNR = randomPNR();
		BagTransaction tempBagTransaction = new BagTransaction(myBagTag, myPNR, Entity.NIL.name(), Entity.PAX.name(), 0, "0");
		System.out.println("\nBlock");
		System.out.println(tempBagTransaction.toJsonString());
	}

	public void demo2() {

		String currentBagBlockHash = "0";

		String myBagTag = randomBagTagID();
		String myPNR = randomPNR();

		ArrayList<BagTransaction> allBagTransactions = new ArrayList<BagTransaction>();
		BagTransaction tempBagTransaction = null;

		// Print Bag Tag (Genesis Block) --------------------------------------
		tempBagTransaction = new BagTransaction(myBagTag, myPNR, Entity.NIL.name(), Entity.PAX.name(), 0, "0");
		currentBagBlockHash = tempBagTransaction.getHash();
		allBagTransactions.add(tempBagTransaction);

		// Bag Drop --------------------------------------
		tempBagTransaction = new BagTransaction(myBagTag, myPNR, Entity.PAX.name(), Entity.AIRP.name(), 1,
				currentBagBlockHash);
		currentBagBlockHash = tempBagTransaction.getHash();
		allBagTransactions.add(tempBagTransaction);

		// Bag SEC Scan --------------------------------------
		tempBagTransaction = new BagTransaction(myBagTag, myPNR, Entity.AIRP.name(), Entity.SEC.name(), 2,
				currentBagBlockHash);
		currentBagBlockHash = tempBagTransaction.getHash();
		allBagTransactions.add(tempBagTransaction);
		
		// Bag Loading --------------------------------------
		tempBagTransaction = new BagTransaction(myBagTag, myPNR, Entity.SEC.name(), Entity.GH.name(), 3,
				currentBagBlockHash);
		currentBagBlockHash = tempBagTransaction.getHash();
		allBagTransactions.add(tempBagTransaction);

		// Print all transactions
		for (BagTransaction b : allBagTransactions)
			System.out.println(b.toJsonString());
		
		checkBlockchainIntegrity(allBagTransactions);
		allBagTransactions.get(1).corruptBlockNow();
		checkBlockchainIntegrity(allBagTransactions);

	}

	
public void checkBlockchainIntegrity(ArrayList<BagTransaction> allBagTransactions) {
	// Check Blockchain. Compare recalculated hash with hash attribute stored
	
	for (BagTransaction b : allBagTransactions){
		System.out.print("Block " + b.getBlockID() + " Stored Hash: " + b.getHash() + " -- Calculated Hash:" + b.createHash()); 
		if (b.getHash().equals(b.createHash()))
			System.out.println(" -- OK");
		else
			System.out.println(" -- FAIL. Blockchain broken.");
	}		
	System.out.println(" -- ");
}
	
	public void checkBlockchainIntegrity2(ArrayList<BagTransaction> allBagTransactions) {
		// Check Blockchain. Compare recalculated hash with hash attribute stored
		for (int x = 0; x < allBagTransactions.size(); x++) {
			if (x == 0)
				System.out.println("Block " + x + " Hash: " + allBagTransactions.get(x).createHash());
			else {
				System.out.print("Block " + x + " Hash: " + allBagTransactions.get(x).createHash() + " Previous Hash: " + allBagTransactions.get(x).getPreviousHash()
						+ " -- Previous Hash recalculated: " + allBagTransactions.get(x - 1).createHash());
				if (allBagTransactions.get(x).getPreviousHash().equals(allBagTransactions.get(x - 1).createHash()))
					System.out.println(" -- OK");
				else
					System.out.println(" -- FAIL. Blockchain broken.");
				
			}
		}
		System.out.println(" -- ");
	}

	public void demo1() {

		String myBagTag = randomBagTagID();
		String myPNR = randomPNR();

		String currentBagBlockHash = "";

		// Print Bag Tag (Genesis Block)
		BagTransaction bagTransaction1 = new BagTransaction(myBagTag, myPNR, Entity.NIL.name(), Entity.PAX.name(), 1,
				"0");
		currentBagBlockHash = bagTransaction1.getHash();

		// Bag Drop
		BagTransaction bagTransaction2 = new BagTransaction(myBagTag, myPNR, Entity.PAX.name(), Entity.AIRP.name(), 2,
				currentBagBlockHash);
		currentBagBlockHash = bagTransaction2.getHash();

		// Bag SEC Scan
		BagTransaction bagTransaction3 = new BagTransaction(myBagTag, myPNR, Entity.AIRP.name(), Entity.SEC.name(), 3,
				currentBagBlockHash);
		currentBagBlockHash = bagTransaction3.getHash();

		// Display Transactions
		System.out.println(bagTransaction1);
		System.out.println(bagTransaction2);
		System.out.println(bagTransaction3.toJsonString());

	}

	// HELPER METHODS --------------------------------------------

	public String randomPNR() {
		final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final int N = alphabet.length();

		Random r = new Random();
		StringBuffer tempPNR = new StringBuffer();

		for (int i = 0; i < 6; i++) {
			char nxtChar = alphabet.charAt(r.nextInt(N));
			while ((i == 0) && (Character.isDigit(nxtChar)))
				nxtChar = alphabet.charAt(r.nextInt(N));
			tempPNR.append(nxtChar);
		}
		return tempPNR.toString();
	}

	public String randomBagTagID() {
		String tempBagTag = "";

		long range = 9999999999L;
		Random r = new Random();
		long number = (long) (r.nextDouble() * range);

		tempBagTag = String.format("%010d", number);
		return tempBagTag;
	}

	public enum Entity {

		PAX {
			@Override
			public String toString() {
				return "Passenger";
			}
		},
		GH {
			@Override
			public String toString() {
				return "Groundhandler";
			}
		},
		AIRL {
			@Override
			public String toString() {
				return "Airline";
			}
		},
		AIRP {
			@Override
			public String toString() {
				return "Airport";
			}
		},
		SEC {
			@Override
			public String toString() {
				return "Security";
			}
		},
		NIL {
			@Override
			public String toString() {
				return "nil";
			}
		}

	}

}
