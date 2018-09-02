
// Copyright 2018 Anotherjavadude
//
// BSD LICENSE
// Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

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
