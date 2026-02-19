import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Плагин, отвечающий за конфигурацию buildTypes
 */
class AndroidBuildTypesConventionPlugin : Plugin<Project>
{
    override fun apply(target: Project)
    {
        with(target)
        {
            extensions.configure<ApplicationExtension> {
                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                        signingConfig = signingConfigs.getByName("debug")
                    }
                }
            }
        }
    }
}