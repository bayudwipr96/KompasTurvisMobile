package id.kompas.turvis

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.rxbinding2.widget.RxTextView
import id.kompas.turvis.databinding.ActivityMainBinding
import id.kompas.turvis.utils.CustomChromeTabs

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initView()
    }

    @SuppressLint("CheckResult")
    private fun initView() {
        val linkStream = RxTextView.textChanges(_binding.edtLink)
            .skipInitialValue()
            .map { link ->
                TextUtils.isEmpty(link)
            }
        linkStream.subscribe {
            showButton(it)
        }

        _binding.btnTestLink.setOnClickListener {
            val url = _binding.edtLink.text.toString()
            if (!Patterns.WEB_URL.matcher(url).matches()) {
                _binding.tvLinkError.visibility = View.VISIBLE
            } else {
                _binding.tvLinkError.visibility = View.GONE
                CustomChromeTabs.openCustomChromeTabs(
                    context = this,
                    url = url,
                    title = getString(R.string.txt_turvis),
                    share = false,
                    forceOpenChrometabs = true
                )
            }
        }
    }

    private fun showButton(isNotValid: Boolean) {
        _binding.btnTestLink.isEnabled = !isNotValid
    }
}