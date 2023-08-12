package br.com.senai.view.horario;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.DiaSemana;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.HorarioAtendimentoTableModel;

public class ViewCadastrarHorario extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableHorarios;
	JFormattedTextField ftfAbertura;
	JFormattedTextField ftfFechamento;
	JComboBox<Restaurante> cbRestaurante;
	JComboBox<DiaSemana> cbDiaSemana;

	private HorarioService horarioService;
	private HorarioAtendimento horario;
	
	private RestauranteService restauranteService;
	
	/**
	 * Create the frame.
	 */
	public ViewCadastrarHorario() {
		setTitle("Gerenciar Horários de Atendimento - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		this.horarioService = new HorarioService();
		this.restauranteService = new RestauranteService();
		
		JLabel lblCategoria = new JLabel("Restaurante");
		lblCategoria.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCategoria.setBounds(10, 11, 84, 33);
		contentPane.add(lblCategoria);
		
		cbRestaurante = new JComboBox<Restaurante>();
		cbRestaurante.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();
					
					List<HorarioAtendimento> restaurantes = horarioService.listarPor(restaurante);
					HorarioAtendimentoTableModel model = new HorarioAtendimentoTableModel(restaurantes);
					tableHorarios.setModel(model);
					tableHorarios.updateUI();
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		cbRestaurante.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbRestaurante.setBounds(104, 14, 198, 22);
		contentPane.add(cbRestaurante);
		
		cbDiaSemana = new JComboBox<DiaSemana>();
		cbDiaSemana.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbDiaSemana.setBounds(481, 14, 198, 22);
		contentPane.add(cbDiaSemana);
		
		JLabel lblCategoria_1 = new JLabel("Dia da Semana");
		lblCategoria_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoria_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCategoria_1.setBounds(359, 11, 110, 33);
		contentPane.add(lblCategoria_1);
		
		JLabel lblCategoria_2 = new JLabel("Abertura");
		lblCategoria_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoria_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCategoria_2.setBounds(-17, 55, 84, 33);
		contentPane.add(lblCategoria_2);
		
		JLabel lblCategoria_2_1 = new JLabel("Fechamento");
		lblCategoria_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoria_2_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCategoria_2_1.setBounds(290, 55, 91, 33);
		contentPane.add(lblCategoria_2_1);
		
		JButton btnNovo = new JButton("Adicionar");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();
					String diaSemana = cbDiaSemana.getSelectedItem().toString();
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
					LocalTime horaAbertura = LocalTime.from(dtf.parse(ftfAbertura.getText()));
					LocalTime  horaFechamento = LocalTime.from(dtf.parse(ftfFechamento.getText()));
					
					if (horario == null) {
						
						horario = new HorarioAtendimento(diaSemana, horaAbertura, horaFechamento, restaurante);
						
					} else {
						
						horario.setDiaSemana(diaSemana);
						horario.setHoraAbertura(horaAbertura);
						horario.setHoraFechamento(horaFechamento);
						horario.setRestaurante(restaurante);
						
					}
					
					tableHorarios.updateUI();
					horarioService.salvar(horario);
					JOptionPane.showMessageDialog(contentPane, "Horário de atendimento salvo com sucesso");
					
					ftfAbertura.setText(null);
					ftfFechamento.setText(null);
					cbDiaSemana.setSelectedIndex(0);
					cbRestaurante.setSelectedIndex(0);
					horario = null;
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnNovo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNovo.setBounds(577, 57, 91, 28);
		contentPane.add(btnNovo);
		
		JLabel lblHorariosEncontradas = new JLabel("Horários encontrados");
		lblHorariosEncontradas.setHorizontalAlignment(SwingConstants.LEFT);
		lblHorariosEncontradas.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblHorariosEncontradas.setBounds(10, 114, 153, 33);
		contentPane.add(lblHorariosEncontradas);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 158, 478, 195);
		contentPane.add(scrollPane);
		
		tableHorarios = new JTable();
		scrollPane.setViewportView(tableHorarios);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "A\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(498, 158, 226, 159);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	
					int linhaSelecionada = tableHorarios.getSelectedRow();
					if (linhaSelecionada >= 0) {
						int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente excluir?", 
								"Exclusão", JOptionPane.YES_NO_OPTION);
						if (opcao == 0) {
							HorarioAtendimentoTableModel model  = (HorarioAtendimentoTableModel) tableHorarios.getModel();
							HorarioAtendimento horarioSelecionado = model.getPor(linhaSelecionada);
							try {
								horarioService.removerPor(horarioSelecionado.getId());
								model.removerPor(linhaSelecionada);
								tableHorarios.updateUI();
								JOptionPane.showMessageDialog(contentPane, "Restaurante removido com sucesso");
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(contentPane, ex.getMessage());
							}
						}
					} else {
						JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para exclusão");
					}
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnExcluir.setBounds(20, 88, 182, 41);
		panel.add(btnExcluir);
		btnExcluir.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					int linhaSelecionada = tableHorarios.getSelectedRow();
					if (linhaSelecionada >= 0) {
						HorarioAtendimentoTableModel model = (HorarioAtendimentoTableModel) tableHorarios.getModel();
						HorarioAtendimento horarioSelecionado = model.getPor(linhaSelecionada);
						setHorarioAtendimento(horarioSelecionado);
						tableHorarios.updateUI();
						JOptionPane.showMessageDialog(contentPane, "Restaurante alterado com sucesso");
					} else {
						JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição");
					}
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnEditar.setBounds(20, 36, 182, 41);
		panel.add(btnEditar);
		btnEditar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ftfAbertura.setText(null);
				ftfFechamento.setText(null);
				cbDiaSemana.setSelectedIndex(0);
				cbRestaurante.setSelectedIndex(0);
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCancelar.setBounds(577, 367, 147, 33);
		contentPane.add(btnCancelar);
		
		ftfAbertura = new JFormattedTextField();
		ftfAbertura.setBounds(73, 61, 183, 20);
		contentPane.add(ftfAbertura);
		
		
		ftfFechamento = new JFormattedTextField();
		ftfFechamento.setBounds(384, 61, 183, 20);
		contentPane.add(ftfFechamento);
		
		try {
			MaskFormatter mascara1 = new MaskFormatter("##:##");
			MaskFormatter mascara2 = new MaskFormatter("##:##");
			mascara1.install(ftfAbertura);
			mascara2.install(ftfFechamento);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.carregarComboDiaSemana();
		this.carregarComboRestaurante();
		
	}
	
	public void carregarComboRestaurante() {
		List<Restaurante> restaurantes = restauranteService.listarTodos();
		for(Restaurante r : restaurantes) {
			this.cbRestaurante.addItem(r);			
		}		
	}

	public void carregarComboDiaSemana() {
		DiaSemana[] diasSemana = DiaSemana.values();
		for(DiaSemana d : diasSemana) {
			this.cbDiaSemana.addItem(d);			
		}		
	}
	
	private void configurarColuna(int indice, int largura) {
		
		this.tableHorarios.getColumnModel().getColumn(indice).setResizable(false);
		this.tableHorarios.getColumnModel().getColumn(indice).setPreferredWidth(largura);
		
	}
	
	@SuppressWarnings("unused")
	private void configurarTabela() {
		
		final int COLUNA_DIA_SEMANA = 0;
		final int COLUNA_ABERTURA = 1;
		final int COLUNA_FECHAMENTO = 2;
		
		this.tableHorarios.getTableHeader().setReorderingAllowed(false);
		this.tableHorarios.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_DIA_SEMANA, 250);
		this.configurarColuna(COLUNA_ABERTURA, 250);
		this.configurarColuna(COLUNA_FECHAMENTO, 250);
	}
	
	public void setHorarioAtendimento(HorarioAtendimento horario) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		String horaAbertura = horario.getHoraAbertura().format(dtf);
		String horaFechamento = horario.getHoraAbertura().format(dtf);
		
		this.ftfAbertura.setText(horaAbertura);
		this.ftfFechamento.setText(horaFechamento);
		this.cbDiaSemana.setSelectedItem(horario.getDiaSemana());
		this.cbRestaurante.setSelectedItem(horario);
		
	}
	
}
