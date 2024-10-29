package com.cmc.glide

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.cmc.glide.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //img from the app
        binding.btnLocal.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.img)
        }

        //img from an Url
        binding.btnUrl.setOnClickListener {
            val url = "https://imgs.search.brave.com/ytgsWquU0mZ_7ZsrP8DL6ify7V9Z98KFoAjK6X4wSMg/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9yYXcu/Z2l0aHVidXNlcmNv/bnRlbnQuY29tL21h/bnVlbGJpZWgvbG9n/by1maWxlLWljb25z/L21hc3Rlci9pY29u/cy9rb3RsaW4uc3Zn"
            Glide.with(this)
                .load(url)
                .error(R.drawable.error)
                .override(300, 300)
                .into(binding.imageView)
        }

        //img upload from the device
        binding.btnUpload.setOnClickListener {
            checkPermissionAndLoadImage()

        }
    }

    // Déclaration du launcher de permission qui sera utilisé pour demander la permission à l'utilisateur
// ActivityResultContracts.RequestPermission() est une API moderne pour gérer les permissions
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) loadImageFromProvider() // Si permission accordée, charge l'image
        else showToast("Permission denied")    // Si refusée, affiche un message
    }

    // Fonction qui vérifie la permission appropriée avant de charger l'image
    private fun checkPermissionAndLoadImage() {
        // Sélectionne la permission appropriée selon la version d'Android
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES     // Pour Android 13+ (API 33+)
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE // Pour les versions antérieures
        }

        // Vérifie le statut de la permission
        when (ContextCompat.checkSelfPermission(this, permission)) {
            PackageManager.PERMISSION_GRANTED -> loadImageFromProvider() // Si permission déjà accordée
            else -> {
                // Si la permission n'est pas accordée, vérifie si on doit montrer une explication
                if (shouldShowRequestPermissionRationale(permission)) {
                    // L'utilisateur a déjà refusé la permission une fois, on explique pourquoi on en a besoin
                    showToast("Storage permission is needed to access images")
                } else {
                    // Première demande ou "Ne plus demander" non coché, lance la demande de permission
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

    // Fonction pour charger l'image depuis le stockage
    private fun loadImageFromProvider() {
        // Crée un objet File pointant vers l'image dans le dossier Downloads
        val imagePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "xml.png"
        )

        // Vérifie si le fichier existe
        if (!imagePath.exists()) {
            showToast("Image file not found")
            return  // Sort de la fonction si le fichier n'existe pas
        }

        // Crée un URI sécurisé pour le fichier en utilisant FileProvider
        // Nécessaire pour Android 7+ pour partager des fichiers entre applications
        val imageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider", // Identifiant unique du FileProvider
            imagePath
        )

        // Affiche l'image dans l'ImageView (binding suppose l'utilisation de View Binding)
        binding.imageView.setImageURI(imageUri)
    }

    // Fonction utilitaire pour afficher les messages Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
