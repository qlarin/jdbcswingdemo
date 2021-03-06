package jdbc.swing.view.audit;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import jdbc.swing.domain.AuditHistory;

public class AuditHistoryTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	public static final int OBJECT_COL = -1;
	public static final int DATE_TIME_COL = 0;
	private static final int ACTION_COL = 1;
	private static final int USER_FIRST_NAME_COL = 2;
	private static final int USER_LAST_NAME_COL = 3;

	private String[] columnNames = { "Date/Time", "Action", "User First Name",
			"User Last Name" };
	
	private List<AuditHistory> auditHistoryList;

	public AuditHistoryTableModel(List<AuditHistory> theAuditHistoryList) {
		auditHistoryList = theAuditHistoryList;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return auditHistoryList.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int rowIndex, int colIndex) {

		AuditHistory tempAuditHistory = auditHistoryList.get(rowIndex);

		switch (colIndex) {
		case DATE_TIME_COL:			
			return tempAuditHistory.getActionDateTime();
		case ACTION_COL:
			return tempAuditHistory.getAction();
		case USER_FIRST_NAME_COL:
			return tempAuditHistory.getUserFirstName();
		case USER_LAST_NAME_COL:
			return tempAuditHistory.getUserLastName();
		case OBJECT_COL:
			return tempAuditHistory;
		default:
			return tempAuditHistory.getUserLastName();
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

}
