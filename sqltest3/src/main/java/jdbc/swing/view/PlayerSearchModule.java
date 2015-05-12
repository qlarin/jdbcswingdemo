package jdbc.swing.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jdbc.swing.dao.PlayerDAO;
import jdbc.swing.dao.UserDAO;
import jdbc.swing.domain.AuditHistory;
import jdbc.swing.domain.Player;
import jdbc.swing.domain.User;
import jdbc.swing.view.audit.AuditHistoryDialog;
import jdbc.swing.view.player.PlayerDialog;
import jdbc.swing.view.player.PlayerTableModel;
import jdbc.swing.view.user.ChangePasswordDialog;
import jdbc.swing.view.user.UserDialog;
import jdbc.swing.view.user.UserLoginDialog;
import jdbc.swing.view.user.UserTableModel;

public class PlayerSearchModule extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField guildNameTextField;
	private JButton buttonSearch;
	private JScrollPane scrollPane;
	private JTable playerTable;

	private UserDAO userDAO;
	private PlayerDAO playerDAO;
	private JPanel playersButtonPanel;
	private JButton btnAddPlayer;
	private JButton btnEditPlayer;
	private JButton btnRemovePlayer;
	private JButton btnShowHistory;

	private int userId;
	private JPanel topPanel;
	private JPanel searchPanel;
	private JLabel lblLoggedIn;
	private JLabel loggedInUserLabel;
	private JPanel playerPanel;
	private JTabbedPane tabbedPane;
	private JPanel usersPanel;
	private JScrollPane userScrollPane;
	private JTable userTable;
	private JPanel panel;
	private JButton addUserButton;
	private JButton updateUser;
	private JButton changePasswordButton;
	private JPanel panel_1;
	
	private boolean admin;
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					PlayerDAO playerDAO = new PlayerDAO();
					UserDAO userDAO = new UserDAO();
					
					List<User> users = userDAO.getUsers(true, 0);

					UserLoginDialog dialog = new UserLoginDialog();
					dialog.populateUsers(users);
					dialog.setPlayerDAO(playerDAO);
					dialog.setUserDAO(userDAO);

					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PlayerSearchModule(int theUserId, boolean theAdmin, PlayerDAO thePlayerDAO, UserDAO theUserDAO) {

		userId = theUserId;
		admin = theAdmin;
		
		playerDAO = thePlayerDAO;
		userDAO = theUserDAO;

		setTitle("Player Search Module");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		playerPanel = new JPanel();
		tabbedPane.addTab("Players", null, playerPanel, null);
		playerPanel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		playerPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		playerTable = new JTable();
		panel_1.add(playerTable.getTableHeader(), BorderLayout.NORTH);
		panel_1.add(playerTable, BorderLayout.CENTER);
		
		searchPanel = new JPanel();
		playerPanel.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setBorder(null);
		FlowLayout flowLayout_1 = (FlowLayout) searchPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);

		JLabel labelEnterGuildName = new JLabel("Enter guild name");
		searchPanel.add(labelEnterGuildName);

		guildNameTextField = new JTextField();
		searchPanel.add(guildNameTextField);
		guildNameTextField.setColumns(10);

		buttonSearch = new JButton("Search");
		searchPanel.add(buttonSearch);
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String guildName = guildNameTextField.getText();
					List<Player> players = null;

					if (guildName != null && guildName.trim().length() > 0) {
						players = playerDAO.searchPlayers(guildName);
					} else {
						players = playerDAO.getAllPlayers();
					}

					PlayerTableModel model = new PlayerTableModel(players);
					playerTable.setModel(model);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		scrollPane = new JScrollPane();

		playersButtonPanel = new JPanel();
		playerPanel.add(playersButtonPanel, BorderLayout.SOUTH);

		btnAddPlayer = new JButton("Add player");
		btnAddPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO, userId);
				dialog.setVisible(true);
			}
		});
		playersButtonPanel.add(btnAddPlayer);

		btnEditPlayer = new JButton("Edit player");
		btnEditPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = playerTable.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"You have to select player", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Player newPlayer = (Player) playerTable.getValueAt(row,
						PlayerTableModel.OBJECT_COL);
				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO, newPlayer, true, userId);
				dialog.setVisible(true);

			}
		});
		playersButtonPanel.add(btnEditPlayer);

		btnRemovePlayer = new JButton("Remove player");
		btnRemovePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int row = playerTable.getSelectedRow();
					if (row < 0) {
						JOptionPane.showMessageDialog(PlayerSearchModule.this,
								"You have to select player", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					int response = JOptionPane.showConfirmDialog(
							PlayerSearchModule.this, "Remove this player?",
							"Confirm", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (response != JOptionPane.YES_OPTION) {
						return;
					}

					Player newPLayer = (Player) playerTable.getValueAt(row,
							PlayerTableModel.OBJECT_COL);
					playerDAO.deletePlayer(newPLayer, userId);
					refreshPlayersView();

					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Player removed succesfully.", "Player Deleted",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error removing player: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		playersButtonPanel.add(btnRemovePlayer);

		btnShowHistory = new JButton("Show History");
		btnShowHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = playerTable.getSelectedRow();

				if (row < 0) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"You have to select player", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Player newPlayer = (Player) playerTable.getValueAt(row,
						PlayerTableModel.OBJECT_COL);

				try {
					int playerId = newPlayer.getId();
					List<AuditHistory> auditHistoryList = playerDAO
							.getAuditHistory(playerId);
					
					AuditHistoryDialog dialog = new AuditHistoryDialog();
					dialog.populate(newPlayer, auditHistoryList);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error retrieving audit history", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		playersButtonPanel.add(btnShowHistory);
	
	usersPanel = new JPanel();
	tabbedPane.addTab("Users", null, usersPanel, null);
	usersPanel.setLayout(new BorderLayout(0, 0));
	
	userScrollPane = new JScrollPane();
	usersPanel.add(userScrollPane, BorderLayout.CENTER);
	
	userTable = new JTable();
	usersPanel.add(userTable.getTableHeader(), BorderLayout.NORTH);
	usersPanel.add(userTable, BorderLayout.CENTER);
	
	panel = new JPanel();
	usersPanel.add(panel, BorderLayout.SOUTH);
	
	addUserButton = new JButton("Add User");
	addUserButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			addUser();
			
		}
	});
	panel.add(addUserButton);
	
	updateUser = new JButton("Update User");
	updateUser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			updateUser();
		}
	});
	panel.add(updateUser);
	
	changePasswordButton = new JButton("Change Password");
	changePasswordButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			changePassword();
		}
	});
	panel.add(changePasswordButton);
	
	topPanel = new JPanel();
	contentPane.add(topPanel, BorderLayout.NORTH);
	FlowLayout flowLayout_2 = (FlowLayout) topPanel.getLayout();
	flowLayout_2.setAlignment(FlowLayout.LEFT);
	
	lblLoggedIn = new JLabel("Logged In:");
	topPanel.add(lblLoggedIn);
	
	loggedInUserLabel = new JLabel("New label");
	topPanel.add(loggedInUserLabel);

	addUserButton.setEnabled(admin);
	
	refreshUsersView();
}
	
	public void refreshUsersView() {

		try {
			List<User> users = userDAO.getUsers(admin, userId);

			UserTableModel model = new UserTableModel(users);

			userTable.setModel(model);
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(this, "Error: " + exc, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void refreshPlayersView() {

		try {
			List<Player> players = playerDAO.getAllPlayers();
			PlayerTableModel model = new PlayerTableModel(players);
			playerTable.setModel(model);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setLoggedInUserName(String firstName, String lastName){
		loggedInUserLabel.setText(firstName + " " + lastName);
	}
	
	private void addUser() {
		UserDialog userDialog = new UserDialog(PlayerSearchModule.this, userDAO, false);
		
		userDialog.setVisible(true);
	}
	
	protected void updateUser() {

		int row = userTable.getSelectedRow();
		
		if (row < 0) {
			JOptionPane.showMessageDialog(PlayerSearchModule.this, "You must select a user", "Error",
					JOptionPane.ERROR_MESSAGE);				
			return;
		}
		
		User tempUser = (User) userTable.getValueAt(row, UserTableModel.OBJECT_COL);
		
		UserDialog dialog = new UserDialog(PlayerSearchModule.this, userDAO, tempUser);

		dialog.setVisible(true);		
	}
	
	protected void changePassword() {
		int row = userTable.getSelectedRow();
		
		if (row < 0) {
			JOptionPane.showMessageDialog(PlayerSearchModule.this, "You must select a user", "Error",
					JOptionPane.ERROR_MESSAGE);				
			return;
		}
		
		User tempUser = (User) userTable.getValueAt(row, UserTableModel.OBJECT_COL);

		ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(tempUser, userDAO);
		
		changePasswordDialog.setVisible(true);
	}
	
	public int getCurrentUserId() {
		return userId;
	}
	
	public void setAdmin(boolean theFlag) {
		admin = theFlag;
		addUserButton.setEnabled(theFlag);		
	}
}
