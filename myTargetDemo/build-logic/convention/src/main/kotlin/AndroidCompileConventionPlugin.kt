import com.android.build.api.dsl.ApplicationExtension
import com.my.target.buildLogic.AndroidSdkVersions
import com.my.target.buildLogic.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Плагин, отвечающий за конфигурацию процесса компиляции
 */
class AndroidCompileConventionPlugin : Plugin<Project>
{
    override fun apply(target: Project)
    {
        with(target)
        {
            extensions.configure<ApplicationExtension> {
                val javaVersion = JavaVersion.toVersion(libs.findVersion("java").get().requiredVersion)

                compileSdk = AndroidSdkVersions.COMPILE_SDK

                compileOptions {
                    sourceCompatibility = javaVersion
                    targetCompatibility = javaVersion
                }

                tasks.withType<KotlinCompile> {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.fromTarget(libs.findVersion("java").get().requiredVersion))
                    }
                }
            }
        }
    }
}