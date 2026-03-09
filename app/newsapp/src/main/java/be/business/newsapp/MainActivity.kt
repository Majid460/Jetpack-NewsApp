package be.business.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import be.business.newsapp.navigation.NewsAppNavDisplay
import be.business.newsapp.ui.theme.AndroidPracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkTheme()
        enableEdgeToEdge()
        setContent {
            AndroidPracticeTheme {
                NewsAppNavDisplay(viewModel)
            }
        }
    }
}