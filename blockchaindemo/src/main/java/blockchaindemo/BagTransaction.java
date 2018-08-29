package blockchaindemo;

import java.time.Instant;
import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BagTransaction {

	private String bagTag;
	private String timeStamp;
	private String pnr;
	private String transferFrom;
	private String transferTo;

	private long blockID;
	private String blockHash;
	private String previousBlockHash;
	private int nonce;

	public BagTransaction(String bagTag, String pnr, String transferFrom, String transferTo, long blockID,
			String previousBlockHash) {
		super();
		this.bagTag = bagTag;
		this.timeStamp = Instant.now().toString();
		this.pnr = pnr;
		this.transferFrom = transferFrom;
		this.transferTo = transferTo;
		this.blockID = blockID;

		this.nonce = 0;
		this.previousBlockHash = previousBlockHash;

		//this.blockHash = createHash();
		this.blockHash = mineHash(7);
	}

	public long getBlockID(){
		return this.blockID;
	}
	
	public String getHash() {
		return this.blockHash;
	}

	public String getPreviousHash() {
		return this.previousBlockHash;
	}

	@Override
	public String toString() {
		return "BagTransaction [bagTag=" + bagTag + ", timeStamp=" + timeStamp + ", pnr=" + pnr + ", transferFrom="
				+ transferFrom + ", transferTo=" + transferTo + ", blockID=" + blockID + ", blockHash=" + blockHash
				+ ", previousBlockHash=" + previousBlockHash + "]";
	}

	public String toJsonString() {

		JSONObject obj = new JSONObject();
		obj.put("pnr", pnr).put("bagTag", bagTag).put("timeStamp", timeStamp).put("blockID", blockID);

		JSONArray list = new JSONArray();
		JSONObject detail = new JSONObject();
		detail.put("transferFrom", transferFrom);
		list.put(detail);
		detail = new JSONObject();
		detail.put("transferTo", transferTo);
		list.put(detail);
		obj.put("custodyTransfer", list);
		obj.put("blockHash", blockHash).put("previousBlockHash", previousBlockHash);

		return obj.toString(2);

	}

	public String createHash() {
		String returnHash = "";

		returnHash = DigestUtils.sha256Hex(this.bagTag + this.timeStamp + this.pnr + this.transferFrom + this.transferTo
				+ this.previousBlockHash + this.nonce);

		return returnHash;
	}

	public void corruptBlockNow() {
		this.transferTo = "NIL";
	}

	public String mineHash(int difficulty) {
		String returnHash = "";
		long countAttempts = 0;
		
		String tempHash = "";
		
		System.out.println("Difficulty: " + difficulty);
		long millisStart = System.currentTimeMillis();
		
		String target = new String(new char[difficulty]).replace('\0', '0');
		tempHash = createHash();

		while (!tempHash.substring(0, difficulty).equals(target)) {
			nonce++;
			tempHash = createHash();
			//System.out.println("  Try Hash: " + tempHash); 
			countAttempts++;
		}

		long millisEnd = System.currentTimeMillis();
		Long duration = millisEnd - millisStart;

		System.out.println("Attempts: " + countAttempts);
		System.out.println("Milliseconds: " + duration);
		
		return tempHash;
	}

}
