package br.com.senai.view.restaurante;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import javax.swing.border.TitledBorder;

public class ViewCadastroRestaurante extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField edtNome;
	JTextArea taDescricao;
	private JTextField edtLogradouro;
	private JTextField edtCidade;
	private JTextField edtComplemento;
	private JTextField edtBairro;
	private JComboBox<Categoria> cbCategoria;
	
	private RestauranteService restauranteService;
	
	private Restaurante restaurante;
	
	private CategoriaService categoriaService;
	
	/**
	 * Create the frame.
	 */
	public ViewCadastroRestaurante() {
		setTitle("Gerenciar Restaurantes - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		this.restauranteService = new RestauranteService();
		this.categoriaService = new CategoriaService();
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaRestaurante view = new ViewConsultaRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnPesquisar.setBounds(577, 11, 147, 33);
		contentPane.add(btnPesquisar);
		
		cbCategoria = new JComboBox<Categoria>();
		cbCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cbCategoria.setBounds(453, 55, 242, 33);
		contentPane.add(cbCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCategoria.setBounds(381, 55, 62, 33);
		contentPane.add(lblCategoria);
		
		edtNome = new JTextField();
		edtNome.setFont(new Font("Tahoma", Font.PLAIN, 11));
		edtNome.setColumns(10);
		edtNome.setBounds(107, 63, 242, 20);
		contentPane.add(edtNome);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNome.setBounds(10, 57, 85, 33);
		contentPane.add(lblNome);
		
		taDescricao = new JTextArea();
		taDescricao.setFont(new Font("Tahoma", Font.PLAIN, 15));
		taDescricao.setBounds(105, 99, 619, 125);
		contentPane.add(taDescricao);
		
		JLabel lblDescricao = new JLabel("Descrição");
		lblDescricao.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDescricao.setBounds(10, 99, 85, 33);
		contentPane.add(lblDescricao);
		
		JLabel lblLogradouro = new JLabel("Logradouro");
		lblLogradouro.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLogradouro.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLogradouro.setBounds(10, 235, 85, 33);
		contentPane.add(lblLogradouro);
		
		edtLogradouro = new JTextField();
		edtLogradouro.setFont(new Font("Tahoma", Font.PLAIN, 15));
		edtLogradouro.setColumns(10);
		edtLogradouro.setBounds(107, 239, 619, 20);
		contentPane.add(edtLogradouro);
		
		JLabel lblCidade = new JLabel("Cidade");
		lblCidade.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCidade.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCidade.setBounds(10, 279, 85, 33);
		contentPane.add(lblCidade);
		
		edtCidade = new JTextField();
		edtCidade.setFont(new Font("Tahoma", Font.PLAIN, 15));
		edtCidade.setColumns(10);
		edtCidade.setBounds(103, 283, 266, 20);
		contentPane.add(edtCidade);
		
		JLabel lblComplemento = new JLabel("Complemento");
		lblComplemento.setHorizontalAlignment(SwingConstants.RIGHT);
		lblComplemento.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblComplemento.setBounds(10, 323, 90, 33);
		contentPane.add(lblComplemento);
		
		edtComplemento = new JTextField();
		edtComplemento.setFont(new Font("Tahoma", Font.PLAIN, 15));
		edtComplemento.setColumns(10);
		edtComplemento.setBounds(107, 327, 619, 20);
		contentPane.add(edtComplemento);
		
		edtBairro = new JTextField();
		edtBairro.setFont(new Font("Tahoma", Font.PLAIN, 15));
		edtBairro.setColumns(10);
		edtBairro.setBounds(453, 283, 271, 20);
		contentPane.add(edtBairro);
		
		JLabel lblBairro = new JLabel("Bairro");
		lblBairro.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBairro.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblBairro.setBounds(381, 279, 62, 33);
		contentPane.add(lblBairro);
		
		JPanel pnlAções = new JPanel();
		pnlAções.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlAções.setBounds(396, 361, 332, 65);
		contentPane.add(pnlAções);
		pnlAções.setLayout(null);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(14, 18, 147, 33);
		pnlAções.add(btnSalvar);
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String nome = edtNome.getText();
					String descricao = taDescricao.getText();
					String logradouro = edtLogradouro.getText();
					String bairro = edtBairro.getText();
					String cidade = edtCidade.getText();
					String complemento = edtComplemento.getText();
					Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
					
					Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
					
					if (restaurante == null) {
						
						restaurante = new Restaurante(nome, descricao, endereco, categoria);
						
					} else {
						
						restaurante.setNome(nome);
						restaurante.setDescricao(descricao);
						restaurante.setEndereco(endereco);
						restaurante.setCategoria(categoria);
						
					}

					restauranteService.salvar(restaurante);
					JOptionPane.showMessageDialog(contentPane, "Restaurante salvo com sucesso");

					edtNome.setText(null);
					taDescricao.setText(null);
					edtLogradouro.setText(null);
					edtBairro.setText(null);
					edtCidade.setText(null);
					edtComplemento.setText(null);
					cbCategoria.setSelectedIndex(0);
					restaurante = null;
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
				
			}
		});
		btnSalvar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(171, 18, 147, 33);
		pnlAções.add(btnCancelar);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edtNome.setText(null);
				taDescricao.setText(null);
				edtLogradouro.setText(null);
				edtBairro.setText(null);
				edtCidade.setText(null);
				edtComplemento.setText(null);
				cbCategoria.setSelectedIndex(0);
				
				restaurante = null;
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		cbCategoria.setBounds(453, 55, 271, 33);
		contentPane.add(cbCategoria);
		
		this.carregarComboCategoria();
	}
	
	public void carregarComboCategoria() {
		List<Categoria> categorias = categoriaService.listarTodas();
		this.cbCategoria.addItem(null);
		for(Categoria c : categorias) {
			this.cbCategoria.addItem(c);			
		}		
	}
	
	public void setRestaurante(Restaurante restaurante) {
		
		this.restaurante = restaurante;
		this.edtNome.setText(restaurante.getNome());
		this.taDescricao.setText(restaurante.getDescricao());
		this.edtCidade.setText(restaurante.getEndereco().getCidade());
		this.edtBairro.setText(restaurante.getEndereco().getBairro());
		this.edtLogradouro.setText(restaurante.getEndereco().getLogradouro());
		this.edtComplemento.setText(restaurante.getEndereco().getComplemento());
		this.cbCategoria.setSelectedItem(restaurante.getCategoria());
		
	}
}
