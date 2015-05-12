package jdbc.swing.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import jdbc.swing.domain.Player;

public class PlayerTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	public static final int OBJECT_COL = -1;
	private static final int NICK_NAME_COL = 0;
	private static final int PROFESSION_COL = 1;
	private static final int GUILD_NAME_COL = 2;
	private static final int INCOME_COL = 3;
	
	private String[] columnNames = {"Nickname", "Profession", "Guild Name", "Income"};
	private List<Player> players;
	
	public PlayerTableModel(List<Player> thePlayers) {
		players = thePlayers;
	}

	public int getRowCount() {
		return players.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Player newPlayer = players.get(rowIndex);
		
		switch(columnIndex){
		case NICK_NAME_COL:
			return newPlayer.getNickName();
		case PROFESSION_COL:
			return newPlayer.getProfession();
		case GUILD_NAME_COL:
			return newPlayer.getGuildName();
		case INCOME_COL:
			return newPlayer.getIncome();
		case OBJECT_COL:
			return newPlayer;
		default:
			return newPlayer.getNickName();
				
		}
	}
	
	public Class getColumnClass(int c){
		return getValueAt(0, c).getClass();
	}
	
}
