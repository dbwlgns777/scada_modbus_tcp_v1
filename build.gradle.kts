plugins {
 java
 application
}

group = "com.zes.device"
version = "0.1.0"

java {
 toolchain {
  languageVersion.set(JavaLanguageVersion.of(17))
  vendor.set(JvmVendorSpec.ADOPTIUM)
 }
}

repositories {
 mavenCentral()
}

dependencies {
 testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
 mainClass.set("com.zes.device.ZES_DeviceApplication")
}

tasks.test {
 useJUnitPlatform()
}
