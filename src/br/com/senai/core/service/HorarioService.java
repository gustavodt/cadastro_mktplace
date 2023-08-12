package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class HorarioService {
	
	private DaoHorario dao;
	
	public HorarioService() {
		this.dao = FactoryDao.getInstance().getDaoHorario();
	}
	
	public void salvar(HorarioAtendimento horario) {
		this.validar(horario);
		
		boolean isJaInserido = horario.getId() > 1;
		if (isJaInserido) {
			this.dao.alterar(horario);
		} else {
			this.dao.inserir(horario);
		}
	}
	
	public void removerPor(int id) {
		if (id > 0) {
			
			this.dao.excluirPor(id);
			
		} else {
			throw new IllegalArgumentException("O id da categoria deve ser maior que zero");
		}
	}
	
	public void validar(HorarioAtendimento horarioNovo) {
		if (horarioNovo != null) {
			
			boolean isRestauranteInvalido = horarioNovo.getRestaurante() == null;
			
			if (isRestauranteInvalido) {
				throw new IllegalArgumentException("O restaurante é obrigatório");
			}
			
			boolean isDiaSemanaInvalido = horarioNovo.getDiaSemana() == null;
			
			if (isDiaSemanaInvalido) {
				throw new IllegalArgumentException("O dia da semana é obrigatório");
			}
			
			LocalTime tempoMax = LocalTime.of(23, 59);
			LocalTime tempoMin = LocalTime.of(00, 00);
			
			boolean isHorariosInvalidos = (horarioNovo.getHoraAbertura().isAfter(tempoMax) || horarioNovo.getHoraAbertura().isBefore(tempoMin))
					|| (horarioNovo.getHoraFechamento().isAfter(tempoMax) || horarioNovo.getHoraFechamento().isBefore(tempoMin));
			
			if (isHorariosInvalidos) {
				throw new IllegalArgumentException("Os horários de abertura e fechamento precisam estar entre 00:00 e 23:59");
			}
			
			List<HorarioAtendimento> horariosExistentes = this.dao.listarTodos();
			for (HorarioAtendimento horarioExistente : horariosExistentes) {
				validarHorario(horarioExistente, horarioNovo);
			}
			
		} else {
			throw new NullPointerException("O horário de atendimento não pode ser nulo");
		}
	}
	
	public void validarHorario(HorarioAtendimento horarioExistente, HorarioAtendimento horarioNovo) {
		if (horarioExistente != null && horarioNovo != null) {
			
			boolean isHorarioNovoInvalido = horarioNovo.getHoraAbertura() == null
					|| (horarioNovo.getHoraAbertura().isAfter(horarioExistente.getHoraAbertura()) && horarioNovo.getHoraAbertura().isBefore(horarioExistente.getHoraFechamento()))
					|| (horarioNovo.getHoraFechamento().isAfter(horarioExistente.getHoraAbertura()) && horarioNovo.getHoraFechamento().isBefore(horarioExistente.getHoraFechamento()))
					|| (horarioNovo.getHoraAbertura().isBefore(horarioExistente.getHoraAbertura()) && horarioNovo.getHoraFechamento().isAfter(horarioExistente.getHoraFechamento()));
			
			if (isHorarioNovoInvalido) {
				throw new IllegalArgumentException("O horário novo não pode entrar em conflito com outros horários existentes");
			}
			
		} else {
			throw new NullPointerException("Os horários de atendimento exitente e novo não podem ser nulos");
		}
	}
	
	public HorarioAtendimento buscarPor(int id) {
		if (id > 0) {
			HorarioAtendimento horarioEncontrado = this.dao.buscarPor(id);
			if (horarioEncontrado == null) {
				throw new IllegalArgumentException("Não foi encontrado horario para o código informado");
			}
			return horarioEncontrado;
		} else {
			throw new IllegalArgumentException("O id do horario deve ser maior que zero");
		}
	}
	
	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		
		if (restaurante != null) {
			return this.dao.listarPor(restaurante);
		} else {
			throw new IllegalArgumentException("O id é obrigatório");
		}
	
	}
	
}
