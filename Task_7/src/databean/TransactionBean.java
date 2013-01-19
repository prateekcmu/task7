package databean;

public class TransactionBean {
	private int transactionid;
	private int customerid;
	private int fundid;
	private double amount;
	private int shares;
	private String transactionType;
	private String execuateDate;
	private String submitTime;
	public int getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(int transactionid) {
		this.transactionid = transactionid;
	}
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	public int getFundid() {
		return fundid;
	}
	public void setFundid(int fundid) {
		this.fundid = fundid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getExecuateDate() {
		return execuateDate;
	}
	public void setExecuateDate(String execuateDate) {
		this.execuateDate = execuateDate;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	
}
