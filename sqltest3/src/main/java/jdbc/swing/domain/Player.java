package jdbc.swing.domain;

import java.math.BigDecimal;

public class Player {

	private int id;
	private String nickName;
	private String profession;
	private String guildName;
	private BigDecimal income;

	public Player(String nickName, String profession, String guildName,
			BigDecimal income) {
		
		this(0, nickName, profession, guildName, income);
	}
	
	public Player(int id, String nickName, String profession, String guildName,
			BigDecimal income) {

		super();
		this.id = id;
		this.nickName = nickName;
		this.profession = profession;
		this.guildName = guildName;
		this.income = income;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", nickName=" + nickName + ", profession="
				+ profession + ", guildName=" + guildName + ", income="
				+ income + "]";
	}

}
