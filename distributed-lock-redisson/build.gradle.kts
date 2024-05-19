import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

plugins {
    id("nu.studer.jooq") version "9.0"
}

val jooqVersion = "3.18.14"
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.mysql:mysql-connector-j")

    // JOOQ 설정
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-codegen:$jooqVersion")
    implementation("org.jooq:jooq-meta:$jooqVersion")
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")
    jooqGenerator("com.mysql:mysql-connector-j")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // redisson
    implementation("org.redisson:redisson-spring-boot-starter:3.20.0")

    runtimeOnly("com.mysql:mysql-connector-j")

    // test
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.8.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
}

jooq {
    version = jooqVersion // default (can be omitted)
    edition.set(JooqEdition.OSS) // default (can be omitted)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                generateSchemaSourceOnCompilation = true // default (can be omitted)
                logging = Logging.INFO
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306/redisson-lock?characterEncoding=UTF-8&serverTimezone=UTC"
                    user = "root"
                    password = "admin"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "redisson-lock"
                        forcedTypes.addAll(listOf(
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "JSONB?"
                            },
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "INET"
                            },
                        ))
                    }
                    generate.apply {
                        isDeprecated = false
                        isTables = true
                        isImmutablePojos = false
                    }
                    target.apply {
                        packageName = "com.example.distributedlockredisson.jooq"
                        directory = "src/main/kotlin/generated"
                        encoding = "UTF-8"
                    }
                    strategy.apply {
                        name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    }
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("generateJooq")
}
