package id.kompas.turvis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.kompas.app.helper.CustomChromeTabs
import id.kompas.turvis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.inibutton.setOnClickListener {
            Toast.makeText(this, "testing", Toast.LENGTH_LONG).show()
            CustomChromeTabs.openCustomChromeTabs(
                this,
                _binding.inputan.text.toString(),
                title = "turvis",
                false,
                forceOpenChrometabs = true
            )
        }
    }
}