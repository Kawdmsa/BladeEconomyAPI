plugins {
    id("java")
}

group = "usa.liez"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.lanink.cn/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("cn.nukkit:Nukkit:MOT-SNAPSHOT")
    compileOnly("com.github.hteppl:DataManager:2.1.0-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}