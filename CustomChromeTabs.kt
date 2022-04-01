package id.kompas.app.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import id.kompas.app.R
import id.kompas.app.service.tracker.BaseTracker
import id.kompas.app.view.ui.webview.WebViewActivity

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
    fun openCustomChromeTabs(context: Context, url: String,
                             title: String, share: Boolean = true,
                             forceOpenChrometabs: Boolean = false,
                             membership: String? = "suber",
    ) {
        val builder = CustomTabsIntent.Builder()
        val customTabColor: CustomTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context,R.color.blue_main))
            .build()

        var mUrl = url
        if(url.contains("interaktif.")){
            mUrl = "$url?open_from=mobile_apps"
        }

        with(builder) {
            val sendIntent: Intent = Intent().apply {
                val extraText = context.getString(R.string.share_download_content)
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, "Kompas.id")
                putExtra(Intent.EXTRA_TEXT, "$extraText")
                type = "text/plain"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share News")

            setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, customTabColor)
            setShowTitle(true)
            setUrlBarHidingEnabled(false)
            setShareState(CustomTabsIntent.SHARE_STATE_ON)
            try {
                val customTabsIntent: CustomTabsIntent = build()

                val headersBundle = Bundle().apply {
                    putString("x-session-membership", membership)
                }

                customTabsIntent.intent.putExtra(android.provider.Browser.EXTRA_HEADERS, headersBundle)

                if (forceOpenChrometabs) customTabsIntent.intent.setPackage("com.android.chrome")
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                customTabsIntent.launchUrl(context, Uri.parse(mUrl))
                setupTrackerOpenCustomChromeTabs(context, title)
            } catch (ex: Exception) {
                BaseTracker.trackNonFatalIssue(ex)
                val newIntent = Intent(context, WebViewActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                newIntent.putExtra("slug", url)
                newIntent.putExtra("title", title)
                context.startActivity(newIntent)
            }
        }
    }

    private fun setupTrackerOpenCustomChromeTabs(context: Context, title: String) {
        when (title) {
            context.getString(R.string.name_financial) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_FINANSIAL, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_lembaga) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_LEMBAGA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_pendidikan) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_PENDIDIKAN, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_otomotif) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_OTOMOTIF, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_property) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_PROPERTI, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_gaya_hidup) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_GAYA_HIDUP, Constant.ClassName.WEBVIEW)

            context.getString(R.string.name_otomotif_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_OTOMOTIF_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_properti_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_PROPERTI_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_finansial_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_FINANSIAL_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_teknologi_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_TEKNOLOGI_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_gaya_hidup_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_GAYA_HIDUP_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_review_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_REVIEW_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_travia_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_INFOGRAFIK_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_stories_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_TAP_STORIES_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_nusantara_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_NUSANTARA_KLASIKA, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_langgam_klasika) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_LANGGAM_KLASIKA, Constant.ClassName.WEBVIEW)

            context.getString(R.string.name_paparan) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_PAPARAN_TOPIK, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_profil) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_PROFIL, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_infografis) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_INFOGRAFIS, Constant.ClassName.WEBVIEW)
            context.getString(R.string.name_data) -> BaseTracker.trackScreenView(Constant.ScreenName.INDEX_DATA, Constant.ClassName.WEBVIEW)
        }
    }
}