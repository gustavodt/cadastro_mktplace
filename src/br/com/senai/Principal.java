package br.com.senai;

import br.com.senai.core.dao.ManagerDb;
import br.com.senai.view.ViewPrincipal;

public class Principal {

	public static void main(String[] args) {
		
		ManagerDb.getInstance();

		System.out.println("Conectou");
		
		new ViewPrincipal().setVisible(true);
		
		System.out.println("Aplicação iniciada com sucesso");
		System.out.println("Aplicação iniciada corretamente");
	}

}
