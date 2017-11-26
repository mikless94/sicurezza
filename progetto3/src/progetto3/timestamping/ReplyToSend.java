package progetto3.timestamping;

import java.io.Serializable;
import java.util.ArrayList;

public class ReplyToSend implements Serializable {
	
	private Reply reply;
	private String signType;
	private byte [] sign;
	/**
	 * @param reply
	 * @param signType
	 * @param sign
	 */
	public ReplyToSend(Reply reply, String signType, byte[] sign) {
		this.reply = reply;
		this.signType = signType;
		this.sign = sign;
	}
	/**
	 * @return the reply
	 */
	public Reply getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(Reply reply) {
		this.reply = reply;
	}
	/**
	 * @return the signType
	 */
	public String getSignType() {
		return signType;
	}
	/**
	 * @param signType the signType to set
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}
	/**
	 * @return the sign
	 */
	public byte[] getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(byte[] sign) {
		this.sign = sign;
	}
	
	
	
	
	
	
	
	
}
