package id.kompas.turvis

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.kompas.turvis.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _url: MutableLiveData<Event<URLState>> = MutableLiveData()
    val url: MutableLiveData<Event<URLState>> get() = _url

    fun checkUrl(url: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (!Patterns.WEB_URL.matcher(url).matches()) {
                _url.postValue(Event(URLState.NotValid))
            } else {
                if (url.contains("http://") || url.contains("https://")) {
                    _url.postValue(Event(URLState.Valid(url)))
                } else {
                    _url.postValue(Event(URLState.NotValid))
                }
            }
        }
    }

    sealed class URLState {
        data class Valid(val url: String) : URLState()
        object NotValid : URLState()
    }
}