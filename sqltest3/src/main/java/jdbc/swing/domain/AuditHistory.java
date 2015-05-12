package jdbc.swing.domain;

import java.util.Date;

public class AuditHistory {

	private int userId;
	private int playerId;
	private String action;
	private Date actionDateTime;

	private String userFirstName;
	private String userLastName;

	public AuditHistory(int userId, int playerId, String action,
			Date actionDateTime, String userFirstName, String userLastName) {
		super();
		this.userId = userId;
		this.playerId = playerId;
		this.action = action;
		this.actionDateTime = actionDateTime;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActionDateTime() {
		return actionDateTime;
	}

	public void setActionDateTime(Date actionDateTime) {
		this.actionDateTime = actionDateTime;
	}

	@Override
	public String toString() {
		return "AuditHistory [userId=" + userId + ", playerId=" + playerId
				+ ", action=" + action + ", actionDateTime=" + actionDateTime
				+ ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + "]";
	}

	
}
