import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

/**
 * Плагин, отвечающий за конфигурацию lint-a
 */
@Suppress("UnstableApiUsage")
class AndroidLintConventionPlugin : Plugin<Project>
{
    override fun apply(target: Project)
    {
        with(target)
        {
            extensions.configure<ApplicationExtension> {
                lint {
                    abortOnError = true
                    warningsAsErrors = true
                    lintConfig = File("../lint.xml")
                }
            }
        }
    }
}