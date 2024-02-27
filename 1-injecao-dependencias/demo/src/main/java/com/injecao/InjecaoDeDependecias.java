package com.injecao;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class InjecaoDeDependecias {
	public static void main(String[] args) {
		SpringApplication.run(InjecaoDeDependecias.class, args);
	}
	
	@Bean
	ApplicationRunner runner(MigracaoUsuario migracaoUsuario) {
		return args -> {
			migracaoUsuario.migrar();
		};
	}
}

@Component
class MigracaoUsuario{
	
	reader<Usuario> reader;
	writer<Usuario> bdw;
	
	public MigracaoUsuario(com.injecao.reader<Usuario> reader, writer<Usuario> bdw) {
		super();
		this.reader = reader;
		this.bdw = bdw;
	}

	void migrar() {
		//ler usuarios em A
		List<Usuario> usuario = reader.read();
		// e escrever em B
		bdw.writer(usuario);
	}
}

record Usuario(String name, String email, String senha) {}

interface reader<T>{
	List<T>read();
}

interface writer<T>{
	void writer(List<T> itens);
}

@Component
class FileReader implements reader<Usuario>{
	public List<Usuario> read(){
		System.out.println("Lendo Usuarios");
		return List.of(new Usuario("Amanda", "amanda123@gmail.com", "amanda321"));
	}
}

@Component
class BdWriter implements writer<Usuario>{
	public void writer(List<Usuario> usuario) {
		System.out.println("Escrevendo os usuarios no banco");
		System.out.println(usuario);
	}
}