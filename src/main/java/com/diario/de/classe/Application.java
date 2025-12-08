package com.diario.de.classe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

	public static void main(String[] args) {
		try {
			System.out.println("Iniciando aplicacao...");
			SpringApplication.run(Application.class, args);
			System.out.println("Aplicacao iniciada com sucesso!");
		} catch (Exception e) {
			System.err.println("Erro ao iniciar aplicacao: " + e.getMessage());
			e.printStackTrace();
		}
	}
}