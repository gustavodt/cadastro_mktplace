package br.com.senai;

import br.com.senai.core.dao.ManagerDb;
import br.com.senai.view.categoria.ViewCadastroCategoria;

public class Principal {

	public static void main(String[] args) {
		
		ManagerDb.getInstance();

		System.out.println("Conectou");
		
		new ViewCadastroCategoria().setVisible(true);
	}

}
