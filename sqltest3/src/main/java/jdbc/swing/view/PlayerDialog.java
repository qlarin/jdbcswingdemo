package jdbc.swing.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import jdbc.swing.dao.PlayerDAO;
import jdbc.swing.domain.Player;

public class PlayerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nickNameTextField;
	private JTextField professionTextField;
	private JTextField guildNameTextField;
	private JTextField incomeTextField;

	private PlayerDAO playerDAO;
	private PlayerSearchModule playerSearchModule;
	
	private Player previousPlayer = null;
	private boolean updateMode = false;

	public PlayerDialog(PlayerSearchModule thePlayerSearchModule,
			PlayerDAO thePlayerDAO, Player thePreviousPlayer, boolean theUpdateMode) {
		this();
		playerDAO = thePlayerDAO;
		playerSearchModule = thePlayerSearchModule;
		
		previousPlayer = thePreviousPlayer;
		updateMode = theUpdateMode;
		
		if(updateMode){
			setTitle("Update player");
			populateGui(previousPlayer);
		}
	}

	private void populateGui(Player thePlayer) {
		nickNameTextField.setText(thePlayer.getNickName());
		professionTextField.setText(thePlayer.getProfession());
		guildNameTextField.setText(thePlayer.getGuildName());
		incomeTextField.setText(thePlayer.getIncome().toString());
	}
	
	public PlayerDialog(PlayerSearchModule thePlayerSearchModule, PlayerDAO thePlayerDAO) {
		this(thePlayerSearchModule, thePlayerDAO, null, false);
	}
	
	/**
	 * Create the dialog.
	 */
	public PlayerDialog() {
		setTitle("Add new player");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		{
			JLabel lblNickname = new JLabel("Nickname");
			contentPanel.add(lblNickname, "2, 2, right, default");
		}
		{
			nickNameTextField = new JTextField();
			contentPanel.add(nickNameTextField, "4, 2, fill, default");
			nickNameTextField.setColumns(10);
		}
		{
			JLabel lblProfession = new JLabel("Profession");
			contentPanel.add(lblProfession, "2, 4, right, default");
		}
		{
			professionTextField = new JTextField();
			contentPanel.add(professionTextField, "4, 4, fill, default");
			professionTextField.setColumns(10);
		}
		{
			JLabel lblGuildName = new JLabel("Guild Name");
			contentPanel.add(lblGuildName, "2, 6, right, default");
		}
		{
			guildNameTextField = new JTextField();
			contentPanel.add(guildNameTextField, "4, 6, fill, default");
			guildNameTextField.setColumns(10);
		}
		{
			JLabel lblIncome = new JLabel("Income");
			contentPanel.add(lblIncome, "2, 8, right, default");
		}
		{
			incomeTextField = new JTextField();
			contentPanel.add(incomeTextField, "4, 8, fill, default");
			incomeTextField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						savePlayer();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void savePlayer() {

		String nickName = nickNameTextField.getText();
		String profession = professionTextField.getText();
		String guildName = guildNameTextField.getText();

		String incomeStr = incomeTextField.getText();
		BigDecimal income = convertStringToBigDecimal(incomeStr);

		Player newPlayer = null;
		
		if(updateMode){
			newPlayer = previousPlayer;
			newPlayer.setNickName(nickName);
			newPlayer.setProfession(profession);
			newPlayer.setGuildName(guildName);
			newPlayer.setIncome(income);
		}else{
			newPlayer = new Player(nickName, profession, guildName, income);
		}
		
		try {
			
			if(updateMode){
				playerDAO.updatePlayer(newPlayer);
			}else{
				playerDAO.addPlayer(newPlayer);
			}
			
			setVisible(false);
			dispose();
			
			playerSearchModule.refreshPlayersView();
			JOptionPane.showMessageDialog(playerSearchModule,
					"Player saved succesfully.", "Player Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(playerSearchModule,
					"Error saving player: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}
	
	protected BigDecimal convertStringToBigDecimal(String incomeStr) {
		BigDecimal result = null;
		
		try {
			double incomeDouble = Double.parseDouble(incomeStr);
			result = BigDecimal.valueOf(incomeDouble);
		}catch(Exception ex){
			System.out.println("Invalid value, defaulting to 0.0");
			result = BigDecimal.valueOf(0.0);
		}
		return result;
	}

}
