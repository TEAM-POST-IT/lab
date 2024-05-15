dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // redisson
    implementation("org.redisson:redisson-spring-boot-starter:3.20.0")

    runtimeOnly("com.mysql:mysql-connector-j")
}
