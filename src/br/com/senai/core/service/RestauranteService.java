package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;

	private DaoHorario daoHorario;

	private DaoCategoria daoCategoria;

	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getdaDaoRestaurante();
		this.daoCategoria = FactoryDao.getInstance().getDaoCategoria();
		this.daoHorario = FactoryDao.getInstance().getDaoHorario();
	}

	public void salvar(Restaurante restaurante) {
		this.validar(restaurante);

		boolean isPersistido = restaurante.getId() > 0;

		if (isPersistido) {
			this.dao.alterar(restaurante);
		} else {
			this.dao.inserir(restaurante);
		}
	}

	private void validar(Restaurante restaurante) {
		if (restaurante != null) {

			if (restaurante.getEndereco() != null) {

				if (restaurante.getCategoria() != null && restaurante.getCategoria().getId() > 0) {

					boolean isNomeInvalido = restaurante.getNome() == null || restaurante.getNome().isBlank()
							|| restaurante.getNome().length() > 250;

					if (isNomeInvalido) {
						throw new IllegalArgumentException(
								"O nome é obrigatório e deve conter menos de 250 caracteres");
					}

					boolean isDescricaoInvalida = restaurante.getDescricao() == null
							|| restaurante.getDescricao().isBlank();

					if (isDescricaoInvalida) {
						throw new IllegalArgumentException("A descrição é obrigatória");
					}

					boolean isLogradouroInvalido = restaurante.getEndereco().getLogradouro() == null
							|| restaurante.getEndereco().getLogradouro().isBlank()
							|| restaurante.getEndereco().getLogradouro().length() > 200;

					if (isLogradouroInvalido) {
						throw new IllegalArgumentException(
								"O logradouro é obrigatório e deve conter menos de 200 caracteres");
					}

					boolean isCidadeInvalida = restaurante.getEndereco().getCidade() == null
							|| restaurante.getEndereco().getCidade().isBlank()
							|| restaurante.getEndereco().getCidade().length() > 50;

					if (isCidadeInvalida) {
						throw new IllegalArgumentException(
								"A cidade do endereço é obrigatória e deve conter menos de 50 caracteres");
					}

					String nomeDoBairro = restaurante.getEndereco() != null ? restaurante.getEndereco().getBairro()
							: "";

					boolean isBairroInvalido = nomeDoBairro.isBlank() || nomeDoBairro.length() > 50;

					if (isBairroInvalido) {
						throw new IllegalArgumentException(
								"O bairro do endereço é obrigatório e não deve possuir mais de 50 caracteres");
					}

				} else {
					throw new NullPointerException("O endereço do restaurante não pode ser nulo");
				}

			} else {
				throw new NullPointerException("O restaurante não pode ser nulo");
			}
		}
	}

	public void removerPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			dao.excluirPor(idDoRestaurante);
		} else {
			throw new IllegalArgumentException("O id para remoção do restaurante deve ser maior que zero");
		}
	}

	public List<Restaurante> listarPor(String nome, Categoria categoria) {

		boolean isCategoriaInformada = categoria != null && categoria.getId() > 0;

		boolean isNomeInformado = nome != null && !nome.isBlank();

		if (isCategoriaInformada && isNomeInformado) {
			throw new IllegalArgumentException("Informe o nome e/ou categoria para listar");
		}

		String filtroNome = "";

		if (isCategoriaInformada) {
			filtroNome = nome + "%";
		} else {
			filtroNome = "%" + nome + "%";
		}

		return dao.listarPor(filtroNome, categoria);

	}

	public List<Restaurante> listarTodos() {
		return dao.listarPor("%%", null);

	}

	public void excluirPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			
			int qtdeDeHorarios = daoHorario.contarPor(idDoRestaurante);
			
			boolean isExisteHorarioVinculado = qtdeDeHorarios > 0;
			
			if(isExisteHorarioVinculado) {
				throw new IllegalArgumentException("Não foi possível excluir o restaurante."
						+ "Motivo: Existem " + qtdeDeHorarios + " horários vinculados ao restaurante");
				
				}
			
			this.dao.excluirPor(idDoRestaurante);
		} else {
			throw new IllegalArgumentException("O id para exclusão deve ser maior que 0");

		}
	}

	public Restaurante buscarPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = this.dao.buscarPor(idDoRestaurante);

			if (restauranteEncontrado == null) {
				throw new IllegalArgumentException("Nenhum restaurante encontrado");
			}

			return restauranteEncontrado;
		} else {
			throw new IllegalArgumentException("O id para busca não pode ser menor que 0");
		}
	}
}
