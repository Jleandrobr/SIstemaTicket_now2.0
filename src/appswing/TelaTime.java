package appswing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.db4o.ObjectContainer;

import modelo.Time;
import regras_negocio.Fachada;




public class TelaTime {

	private JFrame frame;
	private JButton button;
	private JLabel label;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JScrollPane scrollPane;
	private JTable table;
	private ObjectContainer manager;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TelaTime window = new TelaTime();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public TelaTime() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Time");
		frame.setBounds(100, 100, 668, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
				listagem();
			}
			
		});
		frame.getContentPane().setLayout(null);
		
		
		button = new JButton("Criar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField.getText().isEmpty() ||
					   textField_1.getText().isEmpty())
					{
						label_2.setText("campo vazio");
						return;
					}
					String Nome = textField.getText();
					String Origem = textField_1.getText();
					Time time = Fachada.criarTime(Nome, Origem);
					label_2.setText("time criado: "+time.getNome());
					listagem();
				}
				catch(Exception ex) {
					label_2.setText(ex.getMessage());
				}
			}
		});
		button.setBounds(213, 227, 89, 23);
		frame.getContentPane().add(button);
		
		label = new JLabel("Nome do time:");
		label.setBounds(50, 182, 89, 14);
		frame.getContentPane().add(label);
		
		textField = new JTextField();
		textField.setBounds(149, 179, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(373, 179, 86, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		label_1 = new JLabel("Origem do time:");
		label_1.setBounds(277, 182, 114, 14);
		frame.getContentPane().add(label_1);
		
		label_2 = new JLabel("");
		label_2.setForeground(new Color(0, 0, 205));
		label_2.setBounds(10, 207, 608, 23);
		frame.getContentPane().add(label_2);
		
		label_3 = new JLabel("");
		label_3.setBounds(10, 146, 328, 14);
		frame.getContentPane().add(label_3);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 492, 127);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		JButton btnNewButton = new JButton("Apagar time");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0){
						String nome = (String) table.getValueAt( table.getSelectedRow(), 0);

						Fachada.apagarTime(nome);
						label_2.setText("Time apagado" );
						listagem();
					}
					else
						label_2.setText("Time nao selecionado");
				}
				catch(Exception ex) {
					label_2.setText(ex.getMessage());
				}
			}
		});
		btnNewButton.setBounds(370, 227, 114, 23);
		frame.getContentPane().add(btnNewButton);
	}
	
	public void listagem() {
		try {
			List<Time> lista = Fachada.listarTimes();

			//model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			
			//colunas
			model.addColumn("nome");
			model.addColumn("origem");
			
			//linhas
			for(Time time : lista) {
				model.addRow(new Object[] { time.getNome(), time.getOrigem()});
			}
			
			table.setModel(model);
			label_3.setText("resultados: "+lista.size()+ " times  - selecione uma linha");
			
		}
		catch(Exception erro){
			label_2.setText(erro.getMessage());
		}
	}
}
