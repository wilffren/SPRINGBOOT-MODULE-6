package com.example.HU4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hu4Application {

	public static void main(String[] args) {
		SpringApplication.run(Hu4Application.class, args);

		System.out.println("""

				╔══════════════════════════════════════════════════════════════╗
				║                                                              ║
				║       🎪 HU4 - EVENTS & VENUES MANAGEMENT SYSTEM             ║
				║                                                              ║
				║       ✅ Arquitectura Hexagonal                              ║
				║       ✅ Relaciones JPA Optimizadas (OneToMany, ManyToMany)  ║
				║       ✅ Consultas JPQL + Specifications                     ║
				║       ✅ Transacciones @Transactional                        ║
				║       ✅ Migraciones Flyway                                  ║
				║                                                              ║
				║       📡 API REST Endpoints:                                 ║
				║                                                              ║
				║       🎫 EVENTS - http://localhost:8080/api/events           ║
				║          POST   /api/events                                  ║
				║          GET    /api/events                                  ║
				║          GET    /api/events/{id}                             ║
				║          GET    /api/events?venueId=1&status=ACTIVE          ║
				║          PUT    /api/events/{id}                             ║
				║          DELETE /api/events/{id}                             ║
				║                                                              ║
				║       🏛️  VENUES - http://localhost:8080/api/venues          ║
				║          POST   /api/venues                                  ║
				║          GET    /api/venues                                  ║
				║          GET    /api/venues/{id}                             ║
				║          GET    /api/venues/{id}/events                      ║
				║          GET    /api/venues?minCapacity=1000                 ║
				║          PUT    /api/venues/{id}                             ║
				║          DELETE /api/venues/{id}                             ║
				║                                                              ║
				╚══════════════════════════════════════════════════════════════╝

				""");
	}
}