package progetto3.timestamping;

import java.io.Serializable;
import java.util.ArrayList;

public class Reply implements Serializable {
	private String ID;
	private String timestamp;
	private int serialNumber;
	private int timeframeNumber;
	private byte [] rootHash;
	private ArrayList <Info> linkingInfo;
	private String TSAHash;

	
	/**
	 * @param iD
	 * @param timestamp
	 * @param serialNumber
	 * @param timeframeNumber
	 * @param rootHash
	 * @param linkingInfo
	 * @param signType
	 */
	public Reply(String iD, String timestamp, int serialNumber, int timeframeNumber, byte[] rootHash,
			ArrayList<Info> linkingInfo, String TSAHash) {
		ID = iD;
		this.timestamp = timestamp;
		this.serialNumber = serialNumber;
		this.timeframeNumber = timeframeNumber;
		this.rootHash = rootHash;
		this.linkingInfo = linkingInfo;
		this.TSAHash = TSAHash;
	}


	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}


	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}


	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}


	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}


	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}


	/**
	 * @return the timeframeNumber
	 */
	public int getTimeframeNumber() {
		return timeframeNumber;
	}


	/**
	 * @param timeframeNumber the timeframeNumber to set
	 */
	public void setTimeframeNumber(int timeframeNumber) {
		this.timeframeNumber = timeframeNumber;
	}


	/**
	 * @return the rootHash
	 */
	public byte[] getRootHash() {
		return rootHash;
	}


	/**
	 * @param rootHash the rootHash to set
	 */
	public void setRootHash(byte[] rootHash) {
		this.rootHash = rootHash;
	}


	/**
	 * @return the linkingInfo
	 */
	public ArrayList<Info> getLinkingInfo() {
		return linkingInfo;
	}


	/**
	 * @param linkingInfo the linkingInfo to set
	 */
	public void setLinkingInfo(ArrayList<Info> linkingInfo) {
		this.linkingInfo = linkingInfo;
	}


	/**
	 * @return the tSAHash
	 */
	public String getTSAHash() {
		return TSAHash;
	}


	/**
	 * @param tSAHash the tSAHash to set
	 */
	public void setTSAHash(String tSAHash) {
		TSAHash = tSAHash;
	}
	
	
		
		
	
}
