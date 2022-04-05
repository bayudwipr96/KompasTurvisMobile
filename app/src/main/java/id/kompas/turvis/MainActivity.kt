package id.kompas.turvis

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.rxbinding2.widget.RxTextView
import id.kompas.turvis.databinding.ActivityMainBinding
import id.kompas.turvis.utils.CustomChromeTabs

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val _viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initView()
        observeLiveData()
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
            _viewModel.checkUrl(_binding.edtLink.text.toString())
        }
    }

    private fun observeLiveData() {
        _viewModel.url.observe(this) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is MainViewModel.URLState.Valid -> {
                        _binding.tvLinkError.visibility = View.GONE
                        _binding.tfTextInputLink.error = null

                        CustomChromeTabs.openCustomChromeTabs(
                            context = this,
                            url = result.url,
                            title = getString(R.string.txt_turvis),
                            share = false,
                            forceOpenChrometabs = true
                        )
                    }
                    is MainViewModel.URLState.NotValid -> {
                        _binding.tvLinkError.visibility = View.VISIBLE
                        _binding.tfTextInputLink.error = " "
                    }
                }
            }
        }
    }

    private fun showButton(isNotValid: Boolean) {
        _binding.btnTestLink.isEnabled = !isNotValid
    }
}