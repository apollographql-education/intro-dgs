
plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.netflix.dgs.codegen") version "6.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:7.6.0"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.generateJava {
	packageName = "com.example.spotifydemo.generated"
}

