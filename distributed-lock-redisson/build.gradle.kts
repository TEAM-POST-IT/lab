import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Jdbc

plugins {
    id("org.jooq.jooq-codegen-gradle") version "3.19.8"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.mysql:mysql-connector-j")

    // jooq
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqCodegen("com.mysql:mysql-connector-j")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // redisson
    implementation("org.redisson:redisson-spring-boot-starter:3.20.0")

    runtimeOnly("com.mysql:mysql-connector-j")
}

jooq {
    configuration {
        withJdbc(
            Jdbc().apply {
                driver = "com.mysql.cj.jdbc.Driver"
                url = "jdbc:mysql://localhost:3306/redisson-lock?characterEncoding=UTF-8&serverTimezone=UTC"
                user = "root"
                password = "admin"
            }
        )
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.mysql.MySQLDatabase"
                inputSchema = "redisson-lock"
                forcedTypes = listOf(
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
                )
            }
            generate {
                isDeprecated = false
                isRecords = false
                isImmutablePojos = false
                isFluentSetters = false
                // Tell the KotlinGenerator to generate properties in addition to methods for these paths. Default is true.
                isImplicitJoinPathsAsKotlinProperties = true

                // Workaround for Kotlin generating setX() setters instead of setIsX() in byte code for mutable properties called
                // <code>isX</code>. Default is true.
                isKotlinSetterJvmNameAnnotationsOnIsPrefix = true

                // Generate POJOs as data classes, when using the KotlinGenerator. Default is true.
                isPojosAsKotlinDataClasses = true

                // Generate non-nullable types on POJO attributes, where column is not null. Default is false.
                isKotlinNotNullPojoAttributes = false

                // Generate non-nullable types on Record attributes, where column is not null. Default is false.
                isKotlinNotNullRecordAttributes = false

                // Generate non-nullable types on interface attributes, where column is not null. Default is false.
                isKotlinNotNullInterfaceAttributes = false

                // Generate defaulted nullable POJO attributes. Default is true.
                isKotlinDefaultedNullablePojoAttributes = true

                // Generate defaulted nullable Record attributes. Default is true.
                isKotlinDefaultedNullableRecordAttributes = true
            }
            target {
                packageName = "com.example.distributedlockredisson.domain.generated"
                directory = "src/main/kotlin"
                encoding = "UTF-8"
            }
            strategy {
                name = "org.jooq.codegen.DefaultGeneratorStrategy"
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("jooqCodegen")
}
