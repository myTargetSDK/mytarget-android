import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidConfigConventionPluginTest
{
    @Test
    fun `version code includes patch version`()
    {
        assertEquals(5_052_005, getSdkVersionCode("5.52.5"))
    }

    @Test
    fun `version code supports alpha suffix`()
    {
        assertEquals(5_053_000, getSdkVersionCode("5.53.0-alpha01"))
    }

    private fun getSdkVersionCode(version: String): Int
    {
        val method =
            AndroidConfigConventionPlugin::class.java.getDeclaredMethod(
                "getSdkVersionCode",
                String::class.java,
            )
        method.isAccessible = true
        return method.invoke(AndroidConfigConventionPlugin(), version) as Int
    }
}