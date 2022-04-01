package id.kompas.app.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import id.kompas.turvis.R

object CustomChromeTabs {
    /**
     * Open chrome custom tabs
     *
     * Example use: openCustomChromeTabs(this, "https://www.kompas.id", "Harian Kompas")
     *
     * @param context View Context
     *
     * @param url url(string) of article
     *
     * @param title title(string) to display on chrome toolbar
     *
     */
    fun openCustomChromeTabs(
        context: Context, url: String,
        title: String, share: Boolean = true,
        forceOpenChrometabs: Boolean = false,
        membership: String? = "suber",
    ) {
        val builder = CustomTabsIntent.Builder()
        val customTabColor: CustomTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.bahama_blue)).build()

        var mUrl = url
        if (url.contains("interaktif.")) {
            mUrl = "$url?open_from=mobile_apps"
        }

        with(builder) {
            setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, customTabColor)
            setShowTitle(true)
            setUrlBarHidingEnabled(false)
            setShareState(CustomTabsIntent.SHARE_STATE_ON)
            try {
                val customTabsIntent: CustomTabsIntent = build()

                val headersBundle = Bundle().apply {
                    putString("x-session-membership", membership)
                }

                customTabsIntent.intent.putExtra(
                    android.provider.Browser.EXTRA_HEADERS, headersBundle
                )

                if (forceOpenChrometabs) customTabsIntent.intent.setPackage("com.android.chrome")
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                customTabsIntent.launchUrl(context, Uri.parse(mUrl))
            } catch (ex: Exception) {
            }
        }
    }
}