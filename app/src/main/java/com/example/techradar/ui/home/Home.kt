import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Scaffold
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.techradar.R
import com.example.techradar.databinding.FragmentHomeBinding
import com.example.techradar.ui.home.HomeViewModel

class Home : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {











        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
