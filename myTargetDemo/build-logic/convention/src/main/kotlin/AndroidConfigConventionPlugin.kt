import com.android.build.api.dsl.ApplicationExtension
import com.my.target.buildLogic.AndroidSdkVersions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra

/**
 * Плагин, отвечающий за общую конфигурацию `android` блока
 */
@Suppress("UnstableApiUsage")
class AndroidConfigConventionPlugin : Plugin<Project>
{
    override fun apply(target: Project)
    {
        with(target)
        {
            val myTargetSdkVersion = target.rootProject.extra["sdkversion"] as String

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = "com.my.targetDemoApp"
                    minSdk = AndroidSdkVersions.MIN_SDK
                    targetSdk = AndroidSdkVersions.TARGET_SDK
                    versionName = myTargetSdkVersion
                    versionCode = getSdkVersionCode(myTargetSdkVersion)
                    testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
                    vectorDrawables.useSupportLibrary = true
                    multiDexEnabled = true
                }

                buildFeatures {
                    viewBinding = true
                }
            }
        }
    }

    /**
     * Represents integer version of SDK, eg. "5.5.5" becomes "5005005"
     */
    private fun getSdkVersionCode(myTargetSdkVersion: String): Int
    {
        var split : List<String> = myTargetSdkVersion.split(".", limit = 4)
        split = split.take(3)
        if (split.size != 3)
        {
            return 0
        }

        var code = 0
        code += split[0].toInt() * 1_000_000
        code += split[1].toInt() * 1_000
        // Сделано чтобы сборки с версией d.d.d-alpha собирались
        split[2].takeWhile { it.isDigit() }.toInt()
        return code
    }
}