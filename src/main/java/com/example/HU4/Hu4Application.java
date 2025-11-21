package com.example.HU4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal - HU4 Task Management
 * 
 * Historia de Usuario: Administración de Tareas por Usuario con
 * Relaciones y Transacciones Optimizadas
 * 
 * Módulo 6 - Semana 4
 * Story Points: 10
 * 
 * Características implementadas:
 * - TASK 1: Relaciones OneToMany/ManyToOne con JPA
 * - TASK 2: Optimización de consultas (JPQL, Specifications, @EntityGraph)
 * - TASK 3: Transaccionalidad y Migraciones con Flyway
 * 
 * Arquitectura: Hexagonal (Puertos y Adaptadores)
 * Principios: SOLID + Clean Architecture
 */
@SpringBootApplication
public class Hu4Application {

	public static void main(String[] args) {
		SpringApplication.run(Hu4Application.class, args);

		System.out.println("""

				╔══════════════════════════════════════════════════════════════╗
				║                                                              ║
				║       🚀 HU4 - TASK MANAGEMENT SYSTEM                        ║
				║                                                              ║
				║       ✅ Arquitectura Hexagonal                              ║
				║       ✅ Relaciones JPA Optimizadas                          ║
				║       ✅ Consultas JPQL + Specifications                     ║
				║       ✅ Transacciones @Transactional                        ║
				║       ✅ Migraciones Flyway                                  ║
				║                                                              ║
				║       📡 API REST: http://localhost:8080/api/tasks           ║
				║       📊 Endpoints disponibles:                              ║
				║          POST   /api/tasks                                   ║
				║          GET    /api/tasks                                   ║
				║          GET    /api/tasks/{id}                              ║
				║          GET    /api/tasks/search?filters                    ║
				║          GET    /api/tasks/user/{userId}/optimized           ║
				║          PUT    /api/tasks/{id}                              ║
				║          PATCH  /api/tasks/{id}/complete                     ║
				║          DELETE /api/tasks/{id}                              ║
				║                                                              ║
				╚══════════════════════════════════════════════════════════════╝

				""");
	}
}