package petrol;

public class PetrolBean {
	private String date;
	private int money;
	private int kilometre;
	private double consumption;

	public PetrolBean(String date, int money, int kilometre, double consumption) {
		super();
		this.date = date;
		this.money = money;
		this.kilometre = kilometre;
		this.consumption = consumption;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getKilometre() {
		return kilometre;
	}

	public void setKilometre(int kilometre) {
		this.kilometre = kilometre;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

}
