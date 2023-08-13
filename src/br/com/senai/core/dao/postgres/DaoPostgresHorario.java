package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.domain.DiaSemana;

public class DaoPostgresHorario implements DaoHorario {
	
	private final String COUNT_BY_REST = "SELECT Count(*) qtde "
			+ "FROM horarios_atendimento h "
			+ "WHERE h.id_restaurante = ?";
	private final String INSERT = "INSERT INTO horarios_atendimento "
			+ "(dia_semana, hora_abertura, hora_fechamento, id_restaurante) "
			+ "VALUES (?, ?, ?, ?)";
	private final String UPDATE = "UPDATE horarios_atendimento "
			+ "SET dia_semana = ?, hora_abertura = ?, "
			+ "hora_fechamento = ?, id_restaurante = ? "
			+ "WHERE id = ?";
	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";
	private final String SELECT_ALL = 
			"SELECT h.id AS id_horario, h.dia_semana, h.hora_abertura, "
					+ "h.hora_fechamento, h.id_restaurante, "
					+ "r.id AS id_restaurante, r.nome AS nome_restaurante, r.descricao, r.cidade, "
					+ "r.logradouro, r.bairro, r.complemento, "
					+ "c.id AS id_categoria, c.nome AS nome_categoria "
					+ "FROM horarios_atendimento h, restaurantes r, categorias c "
					+ "WHERE h.id_restaurante = r.id "
					+ "AND r.id_categoria = c.id ";
	
	private Connection conexao;
	
	public DaoPostgresHorario() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}
	
	@Override
	public int contarPor(int idDoRestaurante) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
			
			ps = conexao.prepareStatement(COUNT_BY_REST);
			ps.setInt(1, idDoRestaurante);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			
			return 0;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao contar os horários. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	
	}
	
	@Override
	public void inserir(HorarioAtendimento horarioAtendimento) {
		
		PreparedStatement ps = null;
		
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, horarioAtendimento.getDiaSemana().name());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException("Occoreu um erro ao inserir o horário de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}
	
	@Override
	public void alterar(HorarioAtendimento horarioAtendimento) {
		
		PreparedStatement ps = null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, horarioAtendimento.getDiaSemana().name());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.setInt(5, horarioAtendimento.getId());
			ps.execute();
			
			boolean isAlteracaoOk = ps.executeUpdate() == 1;
			
			if (isAlteracaoOk) {
				conexao.commit();
			} else {
				conexao.rollback();
			}
			
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao alterar o horário de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public void excluirPor(int id) {
		
		PreparedStatement ps = null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			
			ps = conexao.prepareStatement(DELETE);
			ps.setInt(1, id);
			
			boolean isExclusaoOk = ps.executeUpdate() == 1;
			
			if (isExclusaoOk) {
				conexao.commit();
			} else {
				conexao.rollback();
			}
			
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao excluir o horário de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public HorarioAtendimento buscarPor(int id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			StringBuilder consulta = new StringBuilder(SELECT_ALL);
			consulta.append(" AND h.id = ? ");
			
			ps = conexao.prepareStatement(consulta.toString());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return extrairDo(rs);
			}
			
			return null;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao buscar o horário de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<HorarioAtendimento> horarios = new ArrayList<HorarioAtendimento>();
		
		try {
			
			StringBuilder consulta = new StringBuilder(SELECT_ALL);
			consulta.append(" AND r.id = ? ");
			
			ps = conexao.prepareStatement(consulta.toString());
			ps.setInt(1, restaurante.getId());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				horarios.add(extrairDo(rs));
			}
			
			return horarios;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao listar os horários de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	@Override
	public List<HorarioAtendimento> listarTodos() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<HorarioAtendimento> horarios = new ArrayList<HorarioAtendimento>();
		
		try {
			ps = conexao.prepareStatement(SELECT_ALL);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				horarios.add(extrairDo(rs));
			}
			
			return horarios;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao listar todos os horários de atendimento. "
					+ "Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	private HorarioAtendimento extrairDo(ResultSet rs) {
		
		try {
			
			int idHorarioAtendimento = rs.getInt("id_horario");
			DiaSemana diaSemana = DiaSemana.valueOf(rs.getString("dia_semana"));
			LocalTime horarioAbertura = rs.getTime("hora_abertura").toLocalTime();
			LocalTime horarioFechamento = rs.getTime("hora_fechamento").toLocalTime();
			
			int idRestaurante = rs.getInt("id_restaurante");
			String nomeRestaurante = rs.getString("nome_restaurante");
			String descricao = rs.getString("descricao");
			String cidade = rs.getString("cidade");
			String logradouro = rs.getString("logradouro");
			String bairro = rs.getString("bairro");
			String complemento = rs.getString("complemento");
			int idCategoria = rs.getInt("id_categoria");
			String nomeCategoria = rs.getString("nome_categoria");
			
			Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
			Categoria categoria = new Categoria(idCategoria, nomeCategoria);
			
			Restaurante restaurante = new Restaurante(idRestaurante, nomeRestaurante, descricao, endereco, categoria);
			
			return new HorarioAtendimento(idHorarioAtendimento, diaSemana, horarioAbertura, horarioFechamento, restaurante);
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao extrair o horário de atendimento. "
					+ "Motivo: " + e.getMessage());
		}
		
	}

}
