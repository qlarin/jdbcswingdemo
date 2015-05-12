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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jdbc.swing.dao.PlayerDAO;
import jdbc.swing.domain.AuditHistory;
import jdbc.swing.domain.Player;
import jdbc.swing.domain.User;

public class PlayerSearchModule extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField guildNameTextField;
	private JButton buttonSearch;
	private JScrollPane scrollPane;
	private JTable table;

	private PlayerDAO playerDAO;
	private JPanel panel_1;
	private JButton btnAddPlayer;
	private JButton btnEditPlayer;
	private JButton btnRemovePlayer;
	private JButton btnShowHistory;

	private int userId;
	private JPanel topPanel;
	private JPanel searchPanel;
	private JLabel lblLoggedIn;
	private JLabel loggedInUserLabel;
	

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					PlayerDAO playerDAO = new PlayerDAO();
					List<User> users = playerDAO.getUsers();

					UserLoginDialog dialog = new UserLoginDialog();
					dialog.populateUsers(users);
					dialog.setPlayerDAO(playerDAO);

					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PlayerSearchModule(int theUserId, PlayerDAO thePlayerDAO) {

		userId = theUserId;
		playerDAO = thePlayerDAO;

		setTitle("Player Search Module");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		topPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) topPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel.add(topPanel, BorderLayout.NORTH);

		lblLoggedIn = new JLabel("Logged In:");
		topPanel.add(lblLoggedIn);

		loggedInUserLabel = new JLabel("New label");
		topPanel.add(loggedInUserLabel);

		searchPanel = new JPanel();
		searchPanel.setBorder(null);
		FlowLayout flowLayout_1 = (FlowLayout) searchPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel.add(searchPanel);

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
					table.setModel(model);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		btnAddPlayer = new JButton("Add player");
		btnAddPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO, userId);
				dialog.setVisible(true);
			}
		});
		panel_1.add(btnAddPlayer);

		btnEditPlayer = new JButton("Edit player");
		btnEditPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = table.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"You have to select player", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Player newPlayer = (Player) table.getValueAt(row,
						PlayerTableModel.OBJECT_COL);
				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO, newPlayer, true, userId);
				dialog.setVisible(true);

			}
		});
		panel_1.add(btnEditPlayer);

		btnRemovePlayer = new JButton("Remove player");
		btnRemovePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int row = table.getSelectedRow();
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

					Player newPLayer = (Player) table.getValueAt(row,
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
		panel_1.add(btnRemovePlayer);

		btnShowHistory = new JButton("Show History");
		btnShowHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = table.getSelectedRow();

				if (row < 0) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"You have to select player", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Player newPlayer = (Player) table.getValueAt(row,
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
		panel_1.add(btnShowHistory);
	}

	public void refreshPlayersView() {

		try {
			List<Player> players = playerDAO.getAllPlayers();
			PlayerTableModel model = new PlayerTableModel(players);
			table.setModel(model);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setLoggedInUserName(String firstName, String lastName){
		loggedInUserLabel.setText(firstName + " " + lastName);
	}
}
